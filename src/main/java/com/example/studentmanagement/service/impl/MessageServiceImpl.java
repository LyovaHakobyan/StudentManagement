package com.example.studentmanagement.service.impl;

import com.example.studentmanagement.entity.Message;
import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.repository.MessageRepository;
import com.example.studentmanagement.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Override
    public List<Message> findAllByUsersId(int user1Id, int user2Id) {
        return messageRepository.findAllByUsersId(user1Id, user2Id);
    }

    @Override
    public void save(User user1, User user2, String messageStr) {
        Message message = new Message();
        message.setUser1(user1);
        message.setUser2(user2);
        message.setMessage(messageStr);
        message.setMessageDate(new Date());
        messageRepository.save(message);
    }
}
