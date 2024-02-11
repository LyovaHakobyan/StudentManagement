package com.example.studentmanagement.controller;

import com.example.studentmanagement.entity.Lesson;
import com.example.studentmanagement.security.SpringUser;
import com.example.studentmanagement.service.LessonService;
import com.example.studentmanagement.util.LessonReqChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @RequestMapping(value = "/lessons", method = RequestMethod.GET)
    public String getLessonsPage(ModelMap modelMap) {
        List<Lesson> lessons = lessonService.findAll();
        modelMap.put("lessons", lessons);
        return "lessons";
    }

    @RequestMapping(value = "/lessons/add", method = RequestMethod.GET)
    public String getLessonAddPage(@RequestParam(value = "error", required = false) String error, ModelMap modelMap) {
        if (error != null) {
            modelMap.put("err", error);
        }
        return "lessonsAdd";
    }

    @RequestMapping(value = "/lessons/add", method = RequestMethod.POST)
    public String addLesson(@ModelAttribute Lesson lesson, @AuthenticationPrincipal SpringUser springUser) {
        if (LessonReqChecker.emptyReq(lesson)) {
            String err = "Empty Request, Try Again";
            return "redirect:/lessons?error=" + err;
        }
        lessonService.save(lesson, springUser.getUser());
        return "redirect:/lessons";
    }

    @RequestMapping(value = "/lessons/delete/{id}", method = RequestMethod.GET)
    public String deleteLesson(@PathVariable int id) {
        Optional<Lesson> byId = lessonService.findById(id);
        if (byId.isEmpty() || byId.get().getId() == 0) {
            return "redirect:/lessons";
        }
        Lesson lesson = byId.get();
        lessonService.delete(lesson);
        return "redirect:/lessons";
    }

    @RequestMapping(value = "/lessons/update/{id}", method = RequestMethod.GET)
    public String getLessonUpdatePage(@RequestParam(value = "error", required = false) String error, @PathVariable int id, ModelMap modelMap) {
        if (error != null) {
            modelMap.put("err", error);
        }
        Optional<Lesson> byId = lessonService.findById(id);
        if (byId.isEmpty() || byId.get().getId() == 0) {
            return "redirect:/lessons";
        }
        Lesson lesson = byId.get();
        modelMap.put("lesson", lesson);
        return "lessonsUpdate";
    }

    @RequestMapping(value = "/lessons/update", method = RequestMethod.POST)
    public String updateLesson(@ModelAttribute Lesson newLesson, @AuthenticationPrincipal SpringUser springUser) {
        if (LessonReqChecker.emptyReq(newLesson)) {
            String err = "Empty Request, Try Again";
            return "redirect:/lessons/update?error=" + err;
        }
        Optional<Lesson> byId = lessonService.findById(newLesson.getId());
        if (byId.isEmpty() || byId.get().getId() == 0) {
            return "redirect:/lessons";
        }
        Lesson oldLesson = byId.get();
        lessonService.update(oldLesson, newLesson, springUser.getUser());
        return "redirect:/lessons";
    }

    @RequestMapping(value = "/lessons/startLesson/{id}", method = RequestMethod.GET)
    public String startLessonPage(@PathVariable("id") int lessonId, @AuthenticationPrincipal SpringUser springUser) {
        Optional<Lesson> byId = lessonService.findById(lessonId);
        if (byId.isEmpty() || byId.get().getId() == 0) {
            return "redirect:/lessons";
        }
        lessonService.startLesson(byId.get(), springUser.getUser());
        return "redirect:/lessons";
    }

}
