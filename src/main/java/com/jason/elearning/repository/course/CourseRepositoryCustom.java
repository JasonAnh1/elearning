package com.jason.elearning.repository.course;

import com.jason.elearning.entity.Course;
import com.jason.elearning.entity.constants.CourseLevel;
import com.jason.elearning.entity.constants.CourseStatus;

import java.util.List;

public interface CourseRepositoryCustom {
    List<Course> getCourse(int page, String title, Long categoryId, Long authorId, String authorName, CourseStatus status, Long startPrice, Long endPrice, Long userId,CourseLevel level);
    Long getEnrollNumber(Long courseId,Long learnerId);
    List<Course> listForUserEnrolled(long categoryId,String status);
    Long countGetCourse( String title,Long categoryId,Long authorId, String authorName, CourseStatus status, Long startPrice, Long endPrice, CourseLevel level);
    List<Course> listByListIds(List<Long> ids);
    List<Course> listAllByTitle(String Title);
}
