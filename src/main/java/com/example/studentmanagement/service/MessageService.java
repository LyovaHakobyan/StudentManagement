package com.example.studentmanagement.service;

import com.example.studentmanagement.entity.Message;
import com.example.studentmanagement.entity.User;

import java.util.List;

public interface MessageService {

    List<Message> findAllByUsersId(int user1Id, int user2Id);

    void save(User user1, User user2, String message);

}
