package com.example.studentmanagement.controller;

import com.example.studentmanagement.entity.Lesson;
import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.entity.UserType;
import com.example.studentmanagement.repository.LessonRepository;
import com.example.studentmanagement.repository.UserRepository;
import com.example.studentmanagement.util.TeacherReqChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Controller
public class TeacherController {

    @Value("${user.pic.file.path}")
    private String picFilePath;

    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;

    @Autowired
    public TeacherController(UserRepository userRepository, LessonRepository lessonRepository) {
        this.userRepository = userRepository;
        this.lessonRepository = lessonRepository;
    }

    @GetMapping("/teachers")
    public String getTeachersPage(ModelMap modelMap) {
        List<User> teachers = userRepository.findAllByUserType(UserType.TEACHER);
        modelMap.put("teachers", teachers);
        return "teachers";
    }

    @GetMapping("/teachers/add")
    public String getTeachersAddPage(ModelMap modelMap) {
        modelMap.put("msg", "");
        return "teachersAdd";
    }

    @PostMapping("/teachers/add")
    public String addTeacher(@ModelAttribute User teacher, @RequestParam("picture") MultipartFile multipartFile, ModelMap modelMap) {
        if (TeacherReqChecker.emptyReq(teacher)) {
            modelMap.put("msg", "Empty Request, Try Again !");
            return "teachersAdd";
        }
        if (userRepository.findUserByEmail(teacher.getEmail()) != null) {
            modelMap.put("msg", "User By This Mail, Already Exists !");
            return "teachersAdd";
        }
        if (!multipartFile.isEmpty() && multipartFile.getSize() > 0) {
            String picName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
            File file = new File(picFilePath + File.separator + picName);
            try {
                multipartFile.transferTo(file);
            } catch (IOException e) {
                return "redirect:/teachers/add";
            }
            teacher.setPicName(picName);
        }
        teacher.setLesson(null);
        teacher.setUserType(UserType.TEACHER);
        userRepository.save(teacher);
        return "redirect:/teachers";
    }

    @GetMapping("/teachers/delete/{id}")
    public String deleteTeacher(@PathVariable int id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isEmpty() || byId.get().getId() == 0) {
            return "redirect:/teachers";
        }
        User teacher = byId.get();
        List<Lesson> allByTeacher = lessonRepository.findAllByTeacher(teacher);
        for (Lesson lesson : allByTeacher) {
            lesson.setTeacher(null);
            lessonRepository.save(lesson);
        }
        userRepository.delete(teacher);
        return "redirect:/teachers";
    }

    @GetMapping("/teachers/update/{id}")
    public String getTeachersUpdatePage(ModelMap modelMap, @PathVariable int id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isEmpty() || byId.get().getId() == 0) {
            return "redirect:/teachers";
        }
        User teacher = byId.get();
        modelMap.put("teacher", teacher);
        modelMap.put("msg", "");
        return "teachersUpdate";
    }

    @PostMapping("/teachers/update")
    public String updateTeacher(@ModelAttribute User teacher, @RequestParam("picture") MultipartFile multipartFile, ModelMap modelMap) {
        Optional<User> byId = userRepository.findById(teacher.getId());
        if (byId.isEmpty() || byId.get().getId() == 0 || TeacherReqChecker.emptyReq(teacher)) {
            return "redirect:/teachers";
        }
        User oldTeacher = byId.get();
        User userByEmail = userRepository.findUserByEmail(teacher.getEmail());
        if (userByEmail != null && userByEmail.getId() != oldTeacher.getId()) {
            modelMap.put("msg", "User By This Mail, Already Exists !");
            modelMap.put("teacher", oldTeacher);
            return "teachersUpdate";
        }
        if (!multipartFile.isEmpty() && multipartFile.getSize() > 0) {
            String picName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
            File file = new File(picFilePath + File.separator + picName);
            try {
                multipartFile.transferTo(file);
            } catch (IOException e) {
                return "redirect:/teachers/add";
            }
            oldTeacher.setPicName(picName);
        }
        oldTeacher.setName(teacher.getName());
        oldTeacher.setSurname(teacher.getSurname());
        oldTeacher.setEmail(teacher.getEmail());
        userRepository.save(oldTeacher);
        return "redirect:/teachers";
    }

}
