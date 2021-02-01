package com.softarex.questionnaire.controllers;

import com.softarex.questionnaire.dto.responses.ResponseWrapper;
import com.softarex.questionnaire.models.Field;
import com.softarex.questionnaire.models.FieldType;
import com.softarex.questionnaire.models.User;
import com.softarex.questionnaire.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
public class QuizController {
    private final UserService userService;

    public QuizController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/fields/list")
    private ResponseWrapper getFields(@AuthenticationPrincipal User user,
                                      HttpServletResponse response
    ) {
        if (user == null) {
            response.setStatus(403);
            return new ResponseWrapper("NOT_AUTHENTICATED", null);
        }
        return user.getFiledList();
    }

    @PostMapping("/fields/add")
    private ResponseWrapper addField(@AuthenticationPrincipal User user,
                                     @RequestParam String label,
                                     @RequestParam String type,
                                     @RequestParam(required = false) String options,
                                     @RequestParam(required = false) boolean required,
                                     @RequestParam(required = false) boolean isActive,
                                     HttpServletResponse response
    ) {
        if (user == null) {
            response.setStatus(403);
            return new ResponseWrapper("NOT_AUTHENTICATED", null);
        }
        user.getFields().add(new Field(label, FieldType.valueOf(type), new ArrayList<String>(Arrays.stream(options.split("\n")).collect(Collectors.toList())), required, isActive));
        User updated = userService.updateUserInDB(user);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(updated, auth.getCredentials(), auth.getAuthorities()));
        return new ResponseWrapper("OK", "Field successfully added");
    }

    @PutMapping("/fields/{id}")
    private ResponseWrapper editField(@AuthenticationPrincipal User user,
                                      @PathVariable Long id,
                                      @RequestParam String label,
                                      @RequestParam String type,
                                      @RequestParam(required = false) String options,
                                      @RequestParam(required = false) boolean required,
                                      @RequestParam(required = false) boolean isActive,
                                      HttpServletResponse response
    ) {
        if (user == null) {
            response.setStatus(403);
            return new ResponseWrapper("NOT_AUTHENTICATED", null);
        }
        if (!user.editField(id, label, FieldType.valueOf(type), new ArrayList<String>(Arrays.stream(options.split("\n")).collect(Collectors.toList())), required, isActive))
            return new ResponseWrapper("NO_SUCH_FIELD", "Field with id \"" + id + "\" not found");
        userService.updateUserInDB(user);
        return new ResponseWrapper("OK", "Field was successfully edited");
    }

    @DeleteMapping("/fields/{id}")
    private ResponseWrapper deleteField(@AuthenticationPrincipal User user,
                                        @PathVariable Long id,
                                        HttpServletResponse response
    ) {
        if (user == null) {
            response.setStatus(403);
            return new ResponseWrapper("NOT_AUTHENTICATED", null);
        }
        if (!user.deleteFieldById(id))
            return new ResponseWrapper("NO_SUCH_FIELD", "Field with id \"" + id + "\" not found");
        userService.updateUserInDB(user);
        return new ResponseWrapper("OK", "Field was successfully deleted");
    }
}