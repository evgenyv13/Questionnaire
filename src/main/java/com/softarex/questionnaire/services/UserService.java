package com.softarex.questionnaire.services;

import com.softarex.questionnaire.dto.EmailChangeRequest;
import com.softarex.questionnaire.dto.PasswordChangeRequest;
import com.softarex.questionnaire.dto.PasswordResetRequest;
import com.softarex.questionnaire.models.User;
import com.softarex.questionnaire.repos.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private HashMap<String, User> unconfirmed = new HashMap<>();
    private List<PasswordResetRequest> passwordResetRequests = new ArrayList<>();
    private List<PasswordChangeRequest> passwordChangeRequests = new ArrayList<>();
    private List<EmailChangeRequest> emailChangeRequests = new ArrayList<>();

    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder, MailService mailService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    public boolean existsByEmail(String email) {
        return userRepo.existsByEmail(email) || unconfirmed.values().stream().anyMatch(el -> el.getEmail().equals(email));
    }

    public void beginSignUp(String email, String password, String firstName, String lastName, String phoneNumber) {
        User user = new User(email, passwordEncoder.encode(password), firstName, lastName, phoneNumber);
        String confirmationCode = UUID.randomUUID().toString();
        unconfirmed.put(confirmationCode, user);
        mailService.sendSignUpConfirmation(email, confirmationCode);
    }

    public Optional<String> confirmSignUp(String code) {
        if (!unconfirmed.keySet().contains(code)) return Optional.empty();
        User confirmed = unconfirmed.get(code);
        unconfirmed.remove(code);
        userRepo.save(confirmed);
        return Optional.of(confirmed.getEmail());
    }

    public void beginPasswordReset(String email) {
        String code = UUID.randomUUID().toString();
        if (!passwordResetRequests.stream().anyMatch(el -> el.getEmail().equals(email))) passwordResetRequests.add(new PasswordResetRequest(email, code));
        mailService.sendPasswordResetLink(email, code);
    }

    public Optional<String> getEmailFromPasswordResetCode(String code) {
        if (!passwordResetRequests.stream().anyMatch(el -> el.getCode().equals(code))) return Optional.empty();
        return passwordResetRequests.stream().filter(el -> el.getCode().equals(code)).findFirst().map(el -> el.getEmail());
    }

    public void resetPassword(String email, String code, String newPassword) {
        if (!passwordResetRequests.stream().anyMatch(el -> el.getCode().equals(code)) || !getEmailFromPasswordResetCode(code).get().equals(email)) return;
        User user = userRepo.findByEmail(email).get();
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepo.save(user);
        passwordResetRequests.removeIf(el -> el.getEmail().equals(email));
    }

    public boolean checkUserPassword(User user, String password) { return passwordEncoder.matches(password, user.getPasswordHash()); }

    public void beginPasswordChange(String email, String newPassword) {
        String code = UUID.randomUUID().toString();
        passwordChangeRequests.add(new PasswordChangeRequest(email, code, newPassword));
        mailService.sendPasswordChangeLink(email, code);
    }

    public  boolean hasPasswordChangeRequest(String email) {
        return passwordChangeRequests.stream().anyMatch(el -> el.getEmail().equals(email));
    }

    public boolean confirmPasswordChange(String email, String code) {
        PasswordChangeRequest req = passwordChangeRequests.stream().filter(el -> el.getEmail().equals(email)).findAny().orElse(null);
        if (req == null || !req.getCode().equals(code)) return false;
        User user = userRepo.findByEmail(email).get();
        user.setPasswordHash(passwordEncoder.encode(req.getNewPassword()));
        userRepo.save(user);
        passwordChangeRequests.remove(req);
        return true;
    }

    public User updateUserParams(String email, String firstName, String lastName, String phoneNumber) {
        User user = userRepo.findByEmail(email).get();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber(phoneNumber);
        userRepo.save(user);
        return user;
    }

    public void beginEmailChange(String oldEmail, String newEmail) {
        String code = UUID.randomUUID().toString();
        emailChangeRequests.add(new EmailChangeRequest(oldEmail, newEmail, code));
        mailService.sendEmailChangeLink(newEmail, code);
    }

    public  boolean hasEmailChangeRequest(String email) {
        return emailChangeRequests.stream().anyMatch(el -> el.getOldEmail().equals(email));
    }

    public Optional<User> confirmEmailChange(String oldEmail, String code) {
        EmailChangeRequest req = emailChangeRequests.stream().filter(el -> el.getOldEmail().equals(oldEmail)).findAny().orElse(null);
        if (req == null || !req.getCode().equals(code)) return Optional.empty();
        User user = userRepo.findByEmail(req.getOldEmail()).get();
        user.setEmail(req.getNewEmail());
        userRepo.save(user);
        emailChangeRequests.remove(req);
        return Optional.of(user);
    }

    public User updateUserInDB(User user) {
        userRepo.save(user);
        return userRepo.findByEmail(user.getEmail()).get();
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
       Optional<User> userOpt = userRepo.findByEmail(s);
       if (!userOpt.isPresent()) throw new UsernameNotFoundException("User with name\"" + s + "\" does not exist");
       return userOpt.get();
    }
}