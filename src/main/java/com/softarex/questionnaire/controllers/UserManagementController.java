package com.softarex.questionnaire.controllers;

import com.softarex.questionnaire.models.User;
import com.softarex.questionnaire.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class UserManagementController {
    private final UserService userService;
    @Value("${password.minlength}")
    private int passwordMinLength;

    public UserManagementController(UserService userService) {
        this.userService = userService;
    }

    private void initHeader(User user, Model model) {
        if (user != null) {
            if (user.getFirstName() != null && !user.getFirstName().equals(""))
                model.addAttribute("firstName", user.getFirstName());
            else model.addAttribute("firstName", "Unknown");
            if (user.getLastName() != null && !user.getLastName().equals(""))
                model.addAttribute("lastName", user.getLastName());
            else model.addAttribute("lastName", "user");
        }
    }

    @GetMapping
    private String getRoot(@AuthenticationPrincipal User user,
                           Model model
    ) {
        initHeader(user, model);
        return "root";
    }

    @GetMapping("/signup")
    private String getSignUp() { return "signup"; }

    @PostMapping("/signup")
    private String postSignUp(@RequestParam String email,
                              @RequestParam String password,
                              @RequestParam String passwordConfirm,
                              @RequestParam(required = false) String firstName,
                              @RequestParam(required = false) String lastName,
                              @RequestParam(required = false) String phoneNumber,
                              Model model
    ) {
        if (userService.existsByEmail(email)) {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "User with such email was already registered");
        } else if (password.length() < passwordMinLength) {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Password length must be at least " + passwordMinLength + " characters");
        } else if (!password.equals(passwordConfirm)) {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Password and it's confirm are not equal");
        } else {
            userService.beginSignUp(email, password, firstName, lastName, phoneNumber);
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "Confirmation link was successfully sent to your email");
        }
        return "signup";
    }

    @GetMapping("/confirm")
    private String getConfirm(@AuthenticationPrincipal User user,
                              @RequestParam String act,
                              @RequestParam String code,
                              Model model
    ) {
        switch (act) {
            case "signup":
                Optional<String> confEmail = userService.confirmSignUp(code);
                if (!confEmail.isPresent()) {
                    model.addAttribute("message", "Wrong confirmation code");
                    return "error_message";
                }
                return "redirect:/login?msgType=confirmed&email=" + confEmail.get();
            case "changepassword":
                if (user == null) return "redirect:/login?msgType=needAuthForPasswordChange";
                if (!userService.hasPasswordChangeRequest(user.getEmail())) {
                    model.addAttribute("message", "You don't have password change request");
                    return "error_message";
                }
                if (userService.confirmPasswordChange(user.getEmail(), code)) {
                    model.addAttribute("messageType", "success");
                    model.addAttribute("message", "Password was successfully changed");
                    return "redirect:../changepassword";
                } else {
                    model.addAttribute("message", "Wrong confirmation code");
                    return "error_message";
                }
            default:
                model.addAttribute("message", "Unknown act \"" + act + "\"!");
                return "error_message";
        }
    }

    @GetMapping("/login")
    private String getLogin(@RequestParam(required = false) String msgType,
                            @RequestParam(required = false) String email,
                            Model model
    ) {
        if (msgType != null && msgType.equals("confirmed")) {
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "Your email was successfully confirmed");
        }
        if (msgType != null && msgType.equals("needAuthForPasswordChange")) {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Your need to be authenticated in order to confirm password change");
        }
        if (email != null) model.addAttribute("email", email);
        return "login";
    }

    @GetMapping("/forgotpassword")
    private String getForgotPassword() { return "forgotpassword"; }

    @PostMapping("/forgotpassword")
    private String postForgotPassword(@RequestParam String email,
                                      Model model
    ) {
        if (!userService.existsByEmail(email)) {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "User with email \"" + email + "\" is not registered");
        } else {
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "Password reset link successfully sent to " + email);
            userService.beginPasswordReset(email);
        }
        return "forgotpassword";
    }

    @GetMapping("/resetpassword")
    private String getResetPassword(@RequestParam String code,
                                    Model model
    ) {
        Optional<String> emailOpt = userService.getEmailFromPasswordResetCode(code);
        if (!emailOpt.isPresent()) {
            model.addAttribute("message", "Wrong code");
            return "error_message";
        }
        model.addAttribute("email", emailOpt.get());
        return "resetpassword";
    }

    @PostMapping("/resetpassword")
    private String postResetPassword(@RequestParam String code,
                                     @RequestParam String password,
                                     @RequestParam String passwordConfirm,
                                     Model model
    ) {
        Optional<String> emailOpt = userService.getEmailFromPasswordResetCode(code);
        if (!emailOpt.isPresent()) {
            model.addAttribute("message", "Wrong code");
            return "error_message";
        }
        if (password.length() < passwordMinLength) {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Password length must be at least " + passwordMinLength + " characters");
            model.addAttribute("email", emailOpt.get());
        } else if (!password.equals(passwordConfirm)) {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Password and it's confirm are not equal");
            model.addAttribute("email", emailOpt.get());
        } else {
            userService.resetPassword(emailOpt.get(), code, password);
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "Password was successfully reset");
        }
        return "resetpassword";
    }

    @GetMapping("/changepassword")
    private String getChangePassword(@AuthenticationPrincipal User user,
                                     Model model
    ) {
        initHeader(user, model);
        return "changepassword";
    }

    @PostMapping("/changepassword")
    private String postChangePassword(@AuthenticationPrincipal User user,
                                      @RequestParam String curPassword,
                                      @RequestParam String newPassword,
                                      @RequestParam String newPasswordConfirm,
                                      Model model
    ) {
        initHeader(user, model);
        if (!userService.checkUserPassword(user, curPassword)) {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Wrong current password");
        } else if (newPassword.length() < passwordMinLength) {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "New password length must be at least " + passwordMinLength + " characters");
        } else if (!newPassword.equals(newPasswordConfirm)) {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "New password and it's confirm are not equal");
        } else  {
            userService.beginPasswordChange(user.getEmail(), newPassword);
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "Confirmation link was successfully sent to your email");
        }
        return "changepassword";
    }
}