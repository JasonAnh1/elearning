package com.jason.elearning.controller;

import com.jason.elearning.configuration.Translator;
import com.jason.elearning.entity.Category;
import com.jason.elearning.entity.response.BaseResponse;
import com.jason.elearning.service.course.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/")
@Transactional
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("v1/create-category")
    public ResponseEntity<?> createCourseCategory(@Valid @RequestBody final Category request) {
        try {
            if(request == null || request.getTitle() == null) {
                throw new Exception(Translator.toLocale("required_fields"));
            }
            return ResponseEntity.ok( categoryService.createCourseCategory(request));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @GetMapping("v1/publish/list-course-categories")
    public ResponseEntity<?> listCourseCategories() {
        try {

            return ResponseEntity.ok( categoryService.listCourseCategory());
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @GetMapping("v1/publish/list-categories")
    public ResponseEntity<?> listAllCategory() {
        try {

            return ResponseEntity.ok( categoryService.listCategories());
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @PostMapping("v1/update-category")
    public ResponseEntity<?> updateCourseCategory(@Valid @RequestBody final Category request) {
        try {
            if(request == null || request.getId() == 0) {
                throw new Exception(Translator.toLocale("required_fields"));
            }
            return ResponseEntity.ok( categoryService.updateCourseCategory(request));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
}
