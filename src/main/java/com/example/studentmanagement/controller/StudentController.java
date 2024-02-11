package com.example.studentmanagement.controller;

import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.entity.UserType;
import com.example.studentmanagement.security.SpringUser;
import com.example.studentmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class StudentController {

    private final UserService userService;

    @RequestMapping(value = "/students", method = RequestMethod.GET)
    public String getStudentsPage(ModelMap modelMap, @AuthenticationPrincipal SpringUser springUser) {
        User user = springUser.getUser();
        if (user.getLesson() != null && user.getUserType().equals(UserType.STUDENT)) {
            List<User> allByLesson = userService.findAllByLesson(user.getLesson());
            modelMap.put("students", allByLesson);
            return "students";
        }
        if (user.getLesson() == null && user.getUserType().equals(UserType.STUDENT)) {
            return "students";
        }
        List<User> students = userService.findAllByUserType(UserType.STUDENT);
        modelMap.put("students", students);
        return "students";
    }

}
