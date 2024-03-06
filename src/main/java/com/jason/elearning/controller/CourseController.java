package com.jason.elearning.controller;

import com.jason.elearning.configuration.Translator;
import com.jason.elearning.entity.*;
import com.jason.elearning.entity.response.BaseResponse;
import com.jason.elearning.repository.course.CourseRepository;
import com.jason.elearning.service.course.CourseCategoryService;
import com.jason.elearning.service.course.CoursePartService;
import com.jason.elearning.service.course.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/")
@Transactional
public class CourseController extends BaseController{

    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseCategoryService categoryService;
    @Autowired
    private CoursePartService coursePartService;
    @PostMapping("v1/create-course")
    public ResponseEntity<?> createCourse(@Valid @RequestBody final Course request) {
        try {
            if(request == null
                    || request.getTitle() == null
                    || request.getCategoryId() == 0
                    || request.getPrice() ==0
                    || request.getPriceSale()==0) {
                throw new Exception(Translator.toLocale("required_fields"));
            }
            return ResponseEntity.ok( courseService.creatCourse(request));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @PostMapping("v1/create-lesson")
    public ResponseEntity<?> createLesson(@Valid @RequestBody final Lesson request) {
        try {
            if(request == null
                    || request.getTitle() == null
                    || request.getCoursePartId() == 0
                    ){
                throw new Exception(Translator.toLocale("required_fields"));
            }
            return ResponseEntity.ok( coursePartService.addLesson(request));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }


    @PostMapping("v1/create-course-section")
    public ResponseEntity<?> createCourseSection(@Valid @RequestBody final CoursePart request) {
        try {
            if(request == null) {
                throw new Exception(Translator.toLocale("required_fields"));
            }
            return ResponseEntity.ok( coursePartService.addSection(request));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @GetMapping("v1/publish/get-course")
    public ResponseEntity<?> getCourseById(@RequestParam(required = false) Long courseId) {
        try {

            return ResponseEntity.ok( courseService.getCourseById(courseId));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @GetMapping("v1/publish/list-course")
    public ResponseEntity<?> listAllCourse(@RequestParam final int page,
                                           @RequestParam(required = false) String title,
                                           @RequestParam(required = false) Long categoryId,
                                           @RequestParam(required = false) Long authorId) {
        try {

            return ResponseEntity.ok( courseService.listCourse(page, categoryId, title, authorId));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @GetMapping("v1/publish/list-course-section")
    public ResponseEntity<?> listAllCourse(@RequestParam final long courseId) {
        try {

            return ResponseEntity.ok( coursePartService.listCourseSession(courseId));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @PostMapping("v1/create-course-category")
    public ResponseEntity<?> createCourseCategory(@Valid @RequestBody final CourseCategory request) {
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
    public ResponseEntity<?> listAllCategories() {
        try {

            return ResponseEntity.ok( categoryService.listCourseCategory());
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @PostMapping("v1/update-course-categories")
    public ResponseEntity<?> updateCourseCategory(@Valid @RequestBody final CourseCategory request) {
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
