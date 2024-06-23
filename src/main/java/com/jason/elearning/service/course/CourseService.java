package com.jason.elearning.service.course;

import com.jason.elearning.entity.Course;
import com.jason.elearning.entity.constants.CourseLevel;
import com.jason.elearning.entity.constants.CourseStatus;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface CourseService {
    Course creatCourse(Course course) throws Exception;
     List<Course> listCourse(int page, Long categoryId, String title, Long authorId, String authorName, CourseStatus status, Long startPrice, Long endPrice, CourseLevel level) throws Exception ;
    Long countListCourse(Long categoryId,String title,Long authorId,String authorName, CourseStatus status,Long startPrice,Long endPrice,CourseLevel level) throws Exception;
    Course getCourseById(Long courseId) throws Exception;
    Course updateCourse(Course course) throws  Exception;
    List<Course>listCourseForUserEnrolled() throws Exception;
    Course promoteCourse(Long courseId) throws Exception;
    List<Course> listAllCourse(String title) throws Exception;
}
