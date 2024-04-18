package com.jason.elearning.service.course;

import com.jason.elearning.entity.Category;

import java.util.List;

public interface CategoryService {
    Category createCourseCategory(Category category) throws Exception;
    List<Category> listCourseCategory() throws Exception;
    Category updateCourseCategory(Category category) throws Exception;
}
