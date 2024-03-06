package com.jason.elearning.service.course;

import com.jason.elearning.entity.CourseCategory;

import java.util.List;

public interface CourseCategoryService {
    CourseCategory createCourseCategory(CourseCategory courseCategory) throws Exception;
    List<CourseCategory> listCourseCategory() throws Exception;
    CourseCategory updateCourseCategory(CourseCategory courseCategory) throws Exception;
}
