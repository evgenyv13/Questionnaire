package com.softarex.questionnaire.services;

import com.softarex.questionnaire.dto.ChangePasswordRequest;
import com.softarex.questionnaire.dto.ResetPasswordRequest;
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
    private List<ResetPasswordRequest> resetPasswordRequests = new ArrayList<>();
    private List<ChangePasswordRequest> changePasswordRequests = new ArrayList<>();

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
        if (!resetPasswordRequests.stream().anyMatch(el -> el.getEmail().equals(email))) resetPasswordRequests.add(new ResetPasswordRequest(email, code));
        mailService.sendPasswordResetLink(email, code);
    }

    public Optional<String> getEmailFromPasswordResetCode(String code) {
        if (!resetPasswordRequests.stream().anyMatch(el -> el.getCode().equals(code))) return Optional.empty();
        return resetPasswordRequests.stream().filter(el -> el.getCode().equals(code)).findFirst().map(el -> el.getEmail());
    }

    public void resetPassword(String email, String code, String newPassword) {
        if (!resetPasswordRequests.stream().anyMatch(el -> el.getCode().equals(code)) || !getEmailFromPasswordResetCode(code).get().equals(email)) return;
        User user = userRepo.findByEmail(email).get();
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepo.save(user);
        resetPasswordRequests.removeIf(el -> el.getEmail().equals(email));
    }

    public boolean checkUserPassword(User user, String password) { return passwordEncoder.matches(password, user.getPasswordHash()); }

    public void beginPasswordChange(String email, String newPassword) {
        String code = UUID.randomUUID().toString();
        changePasswordRequests.add(new ChangePasswordRequest(email, code, newPassword));
        mailService.sendPasswordChangeLink(email, code);
    }

    public  boolean hasPasswordChangeRequest(String email) {
        return changePasswordRequests.stream().anyMatch(el -> el.getEmail().equals(email));
    }

    public boolean confirmPasswordChange(String email, String code) {
        ChangePasswordRequest req = changePasswordRequests.stream().filter(el -> el.getEmail().equals(email)).findAny().orElse(null);
        if (req == null || !req.getCode().equals(code)) return false;
        User user = userRepo.findByEmail(email).get();
        user.setPasswordHash(passwordEncoder.encode(req.getNewPassword()));
        userRepo.save(user);
        changePasswordRequests.remove(req);
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
       Optional<User> userOpt = userRepo.findByEmail(s);
       if (!userOpt.isPresent()) throw new UsernameNotFoundException("User with name\"" + s + "\" does not exist");
       return userOpt.get();
    }
}