package com.jason.elearning.controller;

import com.jason.elearning.entity.User;
import com.jason.elearning.entity.response.BaseResponse;
import com.jason.elearning.service.course.CourseService;
import com.jason.elearning.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/")
@Transactional
public class UserController extends BaseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private UserService userService;

    @GetMapping("v1/list-course-enrolled")
    public ResponseEntity<?> listAllCourse() {
        try {

            return ResponseEntity.ok(new BaseResponse("Success", courseService.listCourseForUserEnrolled()));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }

    @GetMapping("v1/list-users")
    public ResponseEntity<?> listUsers(@RequestParam(required = false) String name) {
        try {

            return ResponseEntity.ok(new BaseResponse("Success", userService.getListUserForOrg(name)));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }

    @GetMapping("v1/info-users")
    public ResponseEntity<?> userInfo (@RequestParam(required = false) Long id) {
        try {

            return ResponseEntity.ok(new BaseResponse("Success", userService.getUserInfo(id)));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @PostMapping("v1/update-users")
    public ResponseEntity<?> updateUserInfo (@Valid @RequestBody final User request) {
        try {

            return ResponseEntity.ok(new BaseResponse("Success", userService.updateProfile(request)));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
}
