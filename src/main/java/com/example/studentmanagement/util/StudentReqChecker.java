package com.example.studentmanagement.util;

import com.example.studentmanagement.entity.User;

public class StudentReqChecker {
    private StudentReqChecker() {
    }

    public static boolean emptyReq(User student) {
        if (student == null) {
            return true;
        } else
            return student.getName().trim().isEmpty() || student.getSurname().trim().isEmpty() || student.getEmail().trim().isEmpty();
    }

}
