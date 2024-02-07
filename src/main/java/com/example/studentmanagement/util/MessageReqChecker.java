package com.example.studentmanagement.util;

public class MessageReqChecker {
    private MessageReqChecker() {
    }

    public static boolean emptyReq(String message) {
        return message == null || message.trim().isEmpty();
    }

}
