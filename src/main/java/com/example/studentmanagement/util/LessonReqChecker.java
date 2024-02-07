package com.example.studentmanagement.util;

import com.example.studentmanagement.entity.Lesson;


public class LessonReqChecker {
    private LessonReqChecker() {
    }

    public static boolean emptyReq(Lesson lesson) {
        if (lesson == null) {
            return true;
        }
        return lesson.getDuration() < 0
                || lesson.getPrice() < 0
                || lesson.getTitle() == null || lesson.getTitle().trim().isEmpty()
                || lesson.getStartDate() == null || lesson.getStartDate().toString().trim().isEmpty();
    }

}
