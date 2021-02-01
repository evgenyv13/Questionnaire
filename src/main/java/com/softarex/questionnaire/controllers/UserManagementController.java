package com.softarex.questionnaire.controllers;

import com.softarex.questionnaire.dto.responses.ResponseWrapper;
import com.softarex.questionnaire.models.User;
import com.softarex.questionnaire.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Optional;

@RestController
public class UserManagementController {
    @Value("${password.minlength}")
    private int passwordMinLength;
    private final UserService userService;

    public UserManagementController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    private ResponseWrapper signUp(@RequestParam String email,
                                   @RequestParam String password,
                                   @RequestParam String confirmPassword,
                                   @RequestParam(required = false) String firstName,
                                   @RequestParam(required = false) String lastName,
                                   @RequestParam(required = false) String phoneNumber
    ) {
        if (userService.existsByEmail(email))
            return new ResponseWrapper("USER_ALREADY_EXISTS", "User with email " + email + " already exists");
        if (password.length() < passwordMinLength)
            return new ResponseWrapper("TOO_SHORT_PASSWORD", "Password length must be at least " + passwordMinLength + " characters");
        if (!password.equals(confirmPassword))
            return new ResponseWrapper("WRONG_PASSWORD_CONFIRM", "Password and its confirm are not equal");
        userService.beginSignUp(email, password, firstName, lastName, phoneNumber);
        return new ResponseWrapper("OK", "Confirmation link was successfully sent to your email");
    }

    @PostMapping("/confirm")
    private ResponseWrapper confirm(@AuthenticationPrincipal User user,
                                    @RequestParam String act,
                                    @RequestParam String code,
                                    HttpServletResponse response
    ) {
        switch (act) {
            case "signup":
                Optional<String> emailOpt = userService.confirmSignUp(code);
                if (!emailOpt.isPresent())
                    return new ResponseWrapper("WRONG_CONFIRMATION_CODE", null);
                return new ResponseWrapper("OK", "Email successfully confirmed");
            case "changeemail":
                if (user == null) {
                    response.setStatus(403);
                    return new ResponseWrapper("NOT_AUTHENTICATED", null);
                }
                if (!userService.hasEmailChangeRequest(user.getEmail()))
                    return new ResponseWrapper("NO_CHANGE_REQUEST", "You don't have any email change request");
                Optional<User> updatedUserOpt =  userService.confirmEmailChange(user.getEmail(), code);
                if (!updatedUserOpt.isPresent())
                    return new ResponseWrapper("WRONG_CONFIRMATION_CODE", null);
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(updatedUserOpt.get(), auth.getCredentials(), auth.getAuthorities()));
                return new ResponseWrapper("OK", "Email was successfully confirmed");
            case "changepassword":
                if (user == null) {
                    response.setStatus(403);
                    return new ResponseWrapper("NOT_AUTHENTICATED", null);
                }
                if (!userService.hasPasswordChangeRequest(user.getEmail()))
                    return new ResponseWrapper("NO_CHANGE_REQUEST", "You don't have any password change request");
                Optional<User> updatedUser = userService.confirmPasswordChange(user.getEmail(), code);
                if (!updatedUser.isPresent())
                    return new ResponseWrapper("WRONG_CONFIRMATION_CODE", null);
                Authentication auth2 = SecurityContextHolder.getContext().getAuthentication();
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(updatedUser.get(), auth2.getCredentials(), auth2.getAuthorities()));
                return new ResponseWrapper("OK", "Password change was successfully confirmed");
            default:
                return new ResponseWrapper("UNKNOWN_ACT", null);
        }
    }

    @PostMapping("/login")
    private ResponseWrapper login(@RequestParam String email,
                                  @RequestParam String password
    ) {
        if (!userService.existsByEmail(email))
            return new ResponseWrapper("USER_DOES_NOT_EXISTS", "User with email " + email + " does not exists");
        if (!userService.verifyUser(email, password))
            return new ResponseWrapper("WRONG_PASSWORD", null);
        User user = userService.getUser(email);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>()));
        return new ResponseWrapper("OK", null);
    }

    @PostMapping("/forgotpassword")
    private ResponseWrapper forgotPassword(@RequestParam String email) {
        if (!userService.existsByEmail(email))
            return new ResponseWrapper("USER_DOES_NOT_EXISTS", "User with email " + email + " does not exists");
        userService.beginPasswordReset(email);
        return new ResponseWrapper("OK", "Confirmation link was successfully sent to your email");
    }

    @PostMapping("/resetpassword")
    private ResponseWrapper resetPassword(@RequestParam String code,
                                          @RequestParam String newPassword
    ) {
        Optional<String> emailOpt = userService.getEmailFromPasswordResetCode(code);
        if (!emailOpt.isPresent())
            return new ResponseWrapper("WRONG_CONFIRMATION_CODE", null);
        if (newPassword.length() < passwordMinLength)
            return new ResponseWrapper("TOO_SHORT_PASSWORD", "Password length must be at least " + passwordMinLength + " characters");
        userService.resetPassword(emailOpt.get(), code, newPassword);
        return new ResponseWrapper("OK", "Password was successfully reset");
    }

    @GetMapping("/profile/current")
    private ResponseWrapper currentProfileInfo(@AuthenticationPrincipal User user,
                                               HttpServletResponse response
    ) {
        if (user == null) {
            response.setStatus(403);
            return new ResponseWrapper("NOT_AUTHENTICATED", null);
        }
        return user.getProfileInfo();
    }

    @PutMapping("/profile")
    private ResponseWrapper editProfile(@AuthenticationPrincipal User user,
                                        @RequestParam(required = false) String firstName,
                                        @RequestParam(required = false) String lastName,
                                        @RequestParam String email,
                                        @RequestParam(required = false) String phoneNumber,
                                        HttpServletResponse response
    ) {
        if (user == null) {
            response.setStatus(403);
            return new ResponseWrapper("NOT_AUTHENTICATED", null);
        }
        User updatedUser = userService.updateUserParams(user.getEmail(), firstName, lastName, phoneNumber);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(updatedUser, auth.getCredentials(), auth.getAuthorities()));
        if (!email.equals(user.getEmail())) {
            userService.beginEmailChange(user.getEmail(), email);
            return new ResponseWrapper("OK", "Profile parameters successfully updated. Confirmation link was sent to " + email + " to confirm email change");
        }
        return new ResponseWrapper("OK",  "Profile parameters successfully updated");
    }

    @PostMapping("/changepassword")
    private ResponseWrapper changePassword(@AuthenticationPrincipal User user,
                                           @RequestParam String curPassword,
                                           @RequestParam String newPassword,
                                           @RequestParam String confirmNewPassword,
                                           HttpServletResponse response
    ) {
        if (user == null) {
            response.setStatus(403);
            return new ResponseWrapper("NOT_AUTHENTICATED", null);
        }
        if (!userService.checkUserPassword(user, curPassword))
            return new ResponseWrapper("WRONG_CURRENT_PASSWORD", null);
        if (newPassword.length() < passwordMinLength)
            return new ResponseWrapper("TOO_SHORT_PASSWORD", "Password length must be at least " + passwordMinLength + " characters");
        if (!newPassword.equals(confirmNewPassword))
            return new ResponseWrapper("WRONG_PASSWORD_CONFIRM", "New password and its confirm are not equal");
        userService.beginPasswordChange(user.getEmail(), newPassword);
        return new ResponseWrapper("OK", "Confirmation link was successfully sent to your email");
    }
}