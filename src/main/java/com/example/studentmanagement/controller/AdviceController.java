package com.example.studentmanagement.controller;

import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.security.SpringUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class AdviceController {

    @ModelAttribute
    public User getUser(@AuthenticationPrincipal SpringUser springUser) {
        if (springUser == null) {
            return null;
        }
        return springUser.getUser();
    }
}
