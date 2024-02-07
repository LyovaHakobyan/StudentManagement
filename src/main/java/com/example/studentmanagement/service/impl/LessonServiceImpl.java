package com.example.studentmanagement.service.impl;

import com.example.studentmanagement.entity.Lesson;
import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.repository.LessonRepository;
import com.example.studentmanagement.repository.UserRepository;
import com.example.studentmanagement.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;

    private final UserRepository userRepository;

    @Override
    public List<Lesson> findAll() {
        return lessonRepository.findAll();
    }

    @Override
    public void save(Lesson lesson, User user) {
        lesson.setTeacher(user);
        lessonRepository.save(lesson);
    }

    @Override
    public void update(Lesson oldLesson, Lesson newLesson, User user) {
        oldLesson.setTitle(newLesson.getTitle());
        oldLesson.setDuration(newLesson.getDuration());
        oldLesson.setPrice(newLesson.getPrice());
        oldLesson.setStartDate(newLesson.getStartDate());
        lessonRepository.save(oldLesson);
    }

    @Override
    public void delete(Lesson lesson) {
        List<User> allByLesson = userRepository.findAllByLesson(lesson);
        for (User user : allByLesson) {
            user.setLesson(null);
            userRepository.save(user);
        }
        lesson.setTeacher(null);
        lessonRepository.save(lesson);
        lessonRepository.delete(lesson);
    }

    @Override
    public Optional<Lesson> findById(int id) {
        return lessonRepository.findById(id);
    }

    @Override
    public void startLesson(Lesson lesson, User user) {
        user.setLesson(lesson);
        userRepository.save(user);
    }

}
