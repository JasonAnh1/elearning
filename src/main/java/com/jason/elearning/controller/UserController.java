package com.jason.elearning.controller;

import com.jason.elearning.entity.response.BaseResponse;
import com.jason.elearning.service.course.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
@Transactional
public class UserController extends BaseController {
    @Autowired
    private CourseService courseService;

    @GetMapping("v1/list-course-enrolled")
    public ResponseEntity<?> listAllCourse() {
        try {

            return ResponseEntity.ok(new BaseResponse("Success", courseService.listCourseForUserEnrolled()));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
}
