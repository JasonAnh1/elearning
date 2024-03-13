package com.jason.elearning.service.course;

import com.jason.elearning.entity.Course;
import com.jason.elearning.entity.CourseCategory;

import java.util.List;

public interface CourseService {
    Course creatCourse(Course course) throws Exception;
    List<Course> listCourse(int page,Long categoryId,String title,Long authorId) throws Exception ;
    Long countListCourse(Long categoryId,String title,Long authorId) throws Exception;
    Course getCourseById(Long courseId) throws Exception;
}
