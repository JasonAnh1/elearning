package com.jason.elearning.controller;

import com.jason.elearning.configuration.Translator;
import com.jason.elearning.entity.Lesson;
import com.jason.elearning.entity.response.BaseResponse;
import com.jason.elearning.service.course.CoursePartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/")
@Transactional
public class LessonController extends BaseController {


    @Autowired
    private CoursePartService coursePartService;

    @PostMapping("v1/update-lesson")
    public ResponseEntity<?> updateLesson(@Valid @RequestBody final Lesson request) {
        try {
            if(request == null) {
                throw new Exception(Translator.toLocale("required_fields"));
            }
            return ResponseEntity.ok( coursePartService.updateLesson(request));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }

}
