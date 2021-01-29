package com.softarex.questionnaire.controllers;

import com.softarex.questionnaire.models.Field;
import com.softarex.questionnaire.models.FieldType;
import com.softarex.questionnaire.models.User;
import com.softarex.questionnaire.services.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;

@Controller
public class QuizController {
    private final UserService userService;

    public QuizController(UserService userService) {
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

    @GetMapping("/fields")
    private void getFields(@AuthenticationPrincipal User user,
                           Model model
    ) {
        initHeader(user, model);
        model.addAttribute("fields", user.getFields());
    }

    @PostMapping("/fields/add")
    private String postFieldsAdd(@AuthenticationPrincipal User user,
                                 @RequestParam String label,
                                 @RequestParam String type,
                                 @RequestParam String options,
                                 @RequestParam(required = false) boolean required,
                                 @RequestParam(required = false) boolean isActive) {
        FieldType fType = null;
        switch (type) {
            case "sl":
                fType = FieldType.SINGLE_LINE_TEXT;
                break;
            case "ml":
                fType = FieldType.MULTI_LINE_TEXT;
                break;
            case "rb":
                fType = FieldType.RADIOBUTTON;
                break;
            case "chb":
                fType = FieldType.CHECKBOX;
                break;
            case "comb":
                fType = FieldType.COMBOBOX;
                break;
            case "dt":
                fType = FieldType.DATE;
                break;
        }
        user.addField(new Field(label, fType, new ArrayList<>(Arrays.asList(options.split("\n"))), required, isActive));
        User updatedUser = userService.updateUserInDB(user);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Authentication updatedAuth = new UsernamePasswordAuthenticationToken(updatedUser, auth.getCredentials(), auth.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(updatedAuth);
        return "redirect:/fields";
    }
}