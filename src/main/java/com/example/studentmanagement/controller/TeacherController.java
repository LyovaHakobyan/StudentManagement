package com.example.studentmanagement.controller;

import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.entity.UserType;
import com.example.studentmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class TeacherController {

    private final UserService userService;

    @GetMapping("/teachers")
    public String getTeachersPage(ModelMap modelMap) {
        List<User> teachers = userService.findAllByUserType(UserType.TEACHER);
        modelMap.put("teachers", teachers);
        return "teachers";
    }

}
