package com.example.studentmanagement.controller;

import com.example.studentmanagement.entity.Lesson;
import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.entity.UserType;
import com.example.studentmanagement.repository.LessonRepository;
import com.example.studentmanagement.repository.UserRepository;
import com.example.studentmanagement.util.StudentReqChecker;
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
public class StudentController {

    @Value("${user.pic.file.path}")
    private String picFilePath;

    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;

    @Autowired
    public StudentController(UserRepository userRepository, LessonRepository lessonRepository) {
        this.userRepository = userRepository;
        this.lessonRepository = lessonRepository;
    }

    @GetMapping("/students")
    public String getStudentsPage(ModelMap modelMap) {
        List<User> students = userRepository.findAllByUserType(UserType.STUDENT);
        modelMap.put("students", students);
        return "students";
    }

    @GetMapping("/students/add")
    public String getStudentAddPage(ModelMap modelMap) {
        List<Lesson> lessons = lessonRepository.findAll();
        modelMap.put("lessons", lessons);
        modelMap.put("msg", "");
        return "studentsAdd";
    }

    @PostMapping("/students/add")
    public String addStudent(@ModelAttribute User student, @RequestParam("picture") MultipartFile multipartFile, @RequestParam("lessonId") int lessonId, ModelMap modelMap) {
        List<Lesson> lessons = lessonRepository.findAll();
        if (StudentReqChecker.emptyReq(student)) {
            modelMap.put("msg", "Empty Request, Try Again !");
            modelMap.put("lessons", lessons);
            return "studentsAdd";
        }
        if (userRepository.findUserByEmail(student.getEmail()) != null) {
            modelMap.put("msg", "User By This Mail, Already Exists !");
            modelMap.put("lessons", lessons);
            return "studentsAdd";
        }
        Optional<Lesson> byId = lessonRepository.findById(lessonId);
        if (byId.isEmpty() || byId.get().getId() == 0) {
            modelMap.put("msg", "Lesson By This Id, Doest Exist !");
            modelMap.put("lessons", lessons);
            return "studentsAdd";
        }
        if (!multipartFile.isEmpty() && multipartFile.getSize() > 0) {
            String picName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
            File file = new File(picFilePath + File.separator + picName);
            try {
                multipartFile.transferTo(file);
            } catch (IOException e) {
                return "redirect:/students/add";
            }
            student.setPicName(picName);
        }
        student.setLesson(byId.get());
        student.setUserType(UserType.STUDENT);
        userRepository.save(student);
        return "redirect:/students";
    }

    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable int id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isEmpty() || byId.get().getId() == 0) {
            return "redirect:/students";
        }
        User student = byId.get();
        student.setLesson(null);
        userRepository.save(student);
        userRepository.delete(student);
        return "redirect:/students";
    }

    @GetMapping("/students/update/{id}")
    public String getStudentUpdatePage(ModelMap modelMap, @PathVariable int id) {
        List<Lesson> lessons = lessonRepository.findAll();
        Optional<User> byId = userRepository.findById(id);
        if (byId.isEmpty() || byId.get().getId() == 0) {
            return "redirect:/students";
        }
        User student = byId.get();
        modelMap.put("student", student);
        modelMap.put("lessons", lessons);
        modelMap.put("msg", "");
        return "studentsUpdate";
    }

    @PostMapping("/students/update")
    public String updateStudent(@ModelAttribute User student, @RequestParam("picture") MultipartFile multipartFile, @RequestParam("lessonId") int lessonId, ModelMap modelMap) {
        Optional<User> byId = userRepository.findById(student.getId());
        List<Lesson> lessons = lessonRepository.findAll();
        if (byId.isEmpty() || byId.get().getId() == 0 || TeacherReqChecker.emptyReq(student)) {
            return "redirect:/students";
        }
        User oldStudent = byId.get();
        User userByEmail = userRepository.findUserByEmail(student.getEmail());
        if (userByEmail != null && userByEmail.getId() != oldStudent.getId()) {
            modelMap.put("msg", "User By This Mail, Already Exists !");
            modelMap.put("student", oldStudent);
            return "studentsUpdate";
        }
        Optional<Lesson> lessonById = lessonRepository.findById(lessonId);
        if (lessonById.isEmpty() || lessonById.get().getId() == 0) {
            modelMap.put("msg", "Lesson By This Id, Doest Exist !");
            modelMap.put("lessons", lessons);
            modelMap.put("student", oldStudent);
            return "studentsUpdate";
        }
        if (!multipartFile.isEmpty() && multipartFile.getSize() > 0) {
            String picName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
            File file = new File(picFilePath + File.separator + picName);
            try {
                multipartFile.transferTo(file);
            } catch (IOException e) {
                return "redirect:/students/add";
            }
            oldStudent.setPicName(picName);
        }
        oldStudent.setName(student.getName());
        oldStudent.setSurname(student.getSurname());
        oldStudent.setEmail(student.getEmail());
        oldStudent.setLesson(lessonById.get());
        userRepository.save(oldStudent);
        return "redirect:/students";
    }

}
