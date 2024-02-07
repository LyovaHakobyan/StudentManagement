package com.example.studentmanagement.service;

import com.example.studentmanagement.entity.Lesson;
import com.example.studentmanagement.entity.User;

import java.util.List;
import java.util.Optional;

public interface LessonService {

    List<Lesson> findAll();

    void save(Lesson lesson, User user);

    void update(Lesson oldLesson, Lesson newLesson, User user);

    void delete(Lesson lesson);

    Optional<Lesson> findById(int id);

    void startLesson(Lesson lesson, User user);

}
