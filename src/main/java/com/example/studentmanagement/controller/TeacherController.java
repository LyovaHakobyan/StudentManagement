package com.example.studentmanagement.controller;

import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.entity.UserType;
import com.example.studentmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class TeacherController {

    private final UserService userService;

    @RequestMapping(value = "/teachers", method = RequestMethod.GET)
    public String getTeachersPage(ModelMap modelMap) {
        List<User> teachers = userService.findAllByUserType(UserType.TEACHER);
        modelMap.put("teachers", teachers);
        return "teachers";
    }

}
