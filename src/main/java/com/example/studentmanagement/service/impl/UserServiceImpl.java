package com.example.studentmanagement.service.impl;

import com.example.studentmanagement.entity.Lesson;
import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.entity.UserType;
import com.example.studentmanagement.repository.UserRepository;
import com.example.studentmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        Optional<User> userByEmail = userRepository.findUserByEmail(email);
        if (userByEmail.isPresent() && userByEmail.get().getEmail().equals(email)) {
            return userByEmail.get();
        }
        return null;
    }

    @Override
    public List<User> findAllByUserType(UserType userType) {
        return userRepository.findAllByUserType(userType);
    }

    @Override
    public List<User> findAllByLesson(Lesson lesson) {
        return userRepository.findAllByLesson(lesson);
    }

    @Override
    public User findById(int id) {
        Optional<User> byId = userRepository.findById(id);
        return byId.orElse(null);
    }
}
