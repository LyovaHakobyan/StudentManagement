package com.example.studentmanagement.controller;

import com.example.studentmanagement.entity.Message;
import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.entity.UserType;
import com.example.studentmanagement.security.SpringUser;
import com.example.studentmanagement.service.MessageService;
import com.example.studentmanagement.service.UserService;
import com.example.studentmanagement.util.MessageReqChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    private final UserService userService;

    @RequestMapping(value = "/students/chat/{toId}", method = RequestMethod.GET)
    public String getChatPage(@PathVariable int toId, @AuthenticationPrincipal SpringUser springUser, ModelMap modelMap) {
        User user2 = userService.findById(toId);
        User user1 = springUser.getUser();
        if (user2 == null || user2.getId() == user1.getId() || user2.getUserType().equals(UserType.TEACHER)) {
            return "redirect:/students";
        }
        List<Message> messages = messageService.findAllByUsersId(user1.getId(), user2.getId());
        modelMap.put("toId", toId);
        modelMap.put("messages", messages);
        return "chat";
    }

    @RequestMapping(value = "/students/chat", method = RequestMethod.POST)
    public String saveMessage(@RequestParam("toId") int toId, @RequestParam("message") String message, @AuthenticationPrincipal SpringUser springUser, ModelMap modelMap) {
        if (MessageReqChecker.emptyReq(message)) {
            return "redirect:/students";
        }
        User user1 = springUser.getUser();
        User user2 = userService.findById(toId);
        if (user2 == null || user2.getId() == user1.getId() || user2.getUserType().equals(UserType.TEACHER)) {
            return "redirect:/students";
        }
        messageService.save(user1, user2, message);
        return "redirect:/students/chat/" + toId;
    }

}
