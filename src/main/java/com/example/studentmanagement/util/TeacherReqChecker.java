package com.example.studentmanagement.util;

import com.example.studentmanagement.entity.User;

public class TeacherReqChecker {
    private TeacherReqChecker() {
    }

    public static boolean emptyReq(User teacher) {
        if (teacher == null) {
            return true;
        } else
            return teacher.getName().trim().isEmpty() || teacher.getSurname().trim().isEmpty() || teacher.getEmail().trim().isEmpty();
    }
}
