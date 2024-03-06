package com.jason.elearning.service.course;

import com.jason.elearning.entity.CoursePart;
import com.jason.elearning.entity.Lesson;

import java.util.List;

public interface CoursePartService {
    CoursePart addSection(CoursePart coursePart);
    List<CoursePart> listCourseSession(long id);
    Lesson addLesson(Lesson lesson);
}
