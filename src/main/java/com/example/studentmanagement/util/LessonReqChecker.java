package com.example.studentmanagement.util;

import com.example.studentmanagement.entity.Lesson;


public class LessonReqChecker {
    private LessonReqChecker() {
    }

    public static boolean emptyReq(Lesson lesson) {
        if (lesson == null) {
            return true;
        } else
            return lesson.getTitle().trim().isEmpty() || lesson.getDuration() < 0 || lesson.getPrice() < 0;
    }

}
