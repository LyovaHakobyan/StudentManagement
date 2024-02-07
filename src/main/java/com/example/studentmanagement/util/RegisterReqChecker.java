package com.example.studentmanagement.util;

import com.example.studentmanagement.entity.User;

public class RegisterReqChecker {
    private RegisterReqChecker() {
    }

    public static boolean emptyReq(User user) {
        if (user == null) {
            return true;
        }
        return user.getName() == null || user.getName().trim().isEmpty()
                || user.getSurname() == null || user.getSurname().trim().isEmpty()
                || user.getEmail() == null || user.getEmail().trim().isEmpty()
                || user.getPassword() == null || user.getPassword().trim().isEmpty()
                || user.getUserType() == null || user.getUserType().toString().trim().isEmpty();
    }

    public static boolean wrongUserType(String userType) {
        return !userType.equals("TEACHER") && !userType.equals("STUDENT");
    }

}
