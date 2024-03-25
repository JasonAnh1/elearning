package com.jason.elearning.service.course;

import com.jason.elearning.entity.Course;
import com.jason.elearning.entity.constants.CourseStatus;

import java.util.List;

public interface CourseService {
    Course creatCourse(Course course) throws Exception;
     List<Course> listCourse(int page, Long categoryId, String title, Long authorId, String authorName, CourseStatus status, Long startPrice, Long endPrice) throws Exception ;
    Long countListCourse(Long categoryId,String title,Long authorId,String authorName, CourseStatus status,Long startPrice,Long endPrice) throws Exception;
    Course getCourseById(Long courseId) throws Exception;
    Course updateCourse(Course course) throws  Exception;
}
