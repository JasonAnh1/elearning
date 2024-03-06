package com.jason.elearning.repository.course;

import com.jason.elearning.entity.CoursePart;

import java.util.List;

public interface CoursePartRepositoryCustom {
    List<CoursePart> getAllCourseSession(Long courseId);
}
