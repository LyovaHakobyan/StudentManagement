package com.example.studentmanagement.service;

import com.example.studentmanagement.entity.Lesson;
import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.entity.UserType;

import java.util.List;

public interface UserService {

    void save(User user);

    User findByEmail(String email);

    List<User> findAllByUserType(UserType userType);

    List<User> findAllByLesson(Lesson lesson);

    User findById(int id);

}
