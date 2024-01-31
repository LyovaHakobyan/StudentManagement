package com.example.studentmanagement.controller;

import com.example.studentmanagement.entity.Lesson;
import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.entity.UserType;
import com.example.studentmanagement.repository.LessonRepository;
import com.example.studentmanagement.repository.UserRepository;
import com.example.studentmanagement.util.DateUtil;
import com.example.studentmanagement.util.LessonReqChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class LessonController {

    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;

    @Autowired
    public LessonController(UserRepository userRepository, LessonRepository lessonRepository) {
        this.userRepository = userRepository;
        this.lessonRepository = lessonRepository;
    }

    @GetMapping("/lessons")
    public String getLessonsPage(ModelMap modelMap) {
        List<Lesson> lessons = lessonRepository.findAll();
        modelMap.put("lessons", lessons);
        return "lessons";
    }

    @GetMapping("/lessons/add")
    public String getLessonAddPage(ModelMap modelMap) {
        List<User> teachers = userRepository.findAllByUserType(UserType.TEACHER);
        modelMap.put("msg", "");
        modelMap.put("teachers", teachers);
        return "lessonsAdd";
    }

    @PostMapping("/lessons/add")
    public String addLesson(@ModelAttribute Lesson lesson, @RequestParam("teacherId") int teacherId, @RequestParam("startingDate") String startingDateStr, ModelMap modelMap) {
        List<User> teachers = userRepository.findAllByUserType(UserType.TEACHER);
        Date date;
        try {
            date = DateUtil.stringToDate(startingDateStr);
        } catch (ParseException e) {
            modelMap.put("msg", "Wong Type Of Date, Try Again !");
            modelMap.put("teachers", teachers);
            return "lessonsAdd";
        }
        if (LessonReqChecker.emptyReq(lesson)) {
            modelMap.put("msg", "Empty Request, Try Again !");
            modelMap.put("teachers", teachers);
            return "lessonsAdd";
        }
        Optional<User> byId = userRepository.findById(teacherId);
        if (byId.isEmpty() || byId.get().getId() == 0 || byId.get().getUserType().equals(UserType.STUDENT)) {
            modelMap.put("msg", "Teacher By This Id, Doest Exist !");
            modelMap.put("teachers", teachers);
            return "lessonsAdd";
        }
        lesson.setStartDate(date);
        lesson.setTeacher(byId.get());
        lessonRepository.save(lesson);
        return "redirect:/lessons";
    }

    @GetMapping("/lessons/delete/{id}")
    public String deleteLesson(@PathVariable int id) {
        Optional<Lesson> byId = lessonRepository.findById(id);
        if (byId.isEmpty() || byId.get().getId() == 0) {
            return "redirect:/lessons";
        }
        Lesson lesson = byId.get();
        List<User> allByLesson = userRepository.findAllByLesson(lesson);
        for (User user : allByLesson) {
            user.setLesson(null);
            userRepository.save(user);
        }
        lesson.setTeacher(null);
        lessonRepository.save(lesson);
        lessonRepository.delete(lesson);
        return "redirect:/lessons";
    }

    @GetMapping("/lessons/update/{id}")
    public String getLessonUpdatePage(ModelMap modelMap, @PathVariable int id) {
        List<User> teachers = userRepository.findAllByUserType(UserType.TEACHER);
        Optional<Lesson> byId = lessonRepository.findById(id);
        if (byId.isEmpty() || byId.get().getId() == 0) {
            return "redirect:/lessons";
        }
        Lesson lesson = byId.get();
        modelMap.put("lesson", lesson);
        modelMap.put("teachers", teachers);
        modelMap.put("msg", "");
        return "lessonsUpdate";
    }

    @PostMapping("/lessons/update")
    public String updateLesson(@ModelAttribute Lesson lesson, @RequestParam("teacherId") int teacherId, @RequestParam("startingDate") String startingDateStr, ModelMap modelMap) {
        Optional<Lesson> byId = lessonRepository.findById(lesson.getId());
        List<User> teachers = userRepository.findAllByUserType(UserType.TEACHER);
        if (byId.isEmpty() || byId.get().getId() == 0 || LessonReqChecker.emptyReq(lesson)) {
            return "redirect:/lessons";
        }
        Lesson oldLesson = byId.get();
        Date date;
        try {
            date = DateUtil.stringToDate(startingDateStr);
            oldLesson.setStartDate(date);
        } catch (ParseException e) {
            modelMap.put("msg", "Wong Type Of Date, Try Again !");
            modelMap.put("lesson", oldLesson);
            modelMap.put("teachers", teachers);
            return "lessonsUpdate";
        }
        Optional<User> userById = userRepository.findById(teacherId);
        if (userById.isEmpty() || userById.get().getId() == 0 || userById.get().getUserType().equals(UserType.STUDENT)) {
            modelMap.put("msg", "Teacher By This Id, Doest Exist !");
            modelMap.put("lesson", oldLesson);
            modelMap.put("teachers", teachers);
            return "lessonsUpdate";
        }
        oldLesson.setTitle(lesson.getTitle());
        oldLesson.setDuration(lesson.getDuration());
        oldLesson.setPrice(lesson.getPrice());
        oldLesson.setTeacher(userById.get());
        lessonRepository.save(oldLesson);
        return "redirect:/lessons";
    }

}
