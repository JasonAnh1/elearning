package com.jason.elearning.repository.course;

import com.jason.elearning.entity.Course;

import java.util.List;

public interface CourseRepositoryCustom {
    List<Course> getCourse(int page, String title,Long categoryId,Long authorId);
    Long getEnrollNumber(Long courseId,Long learnerId);
}
