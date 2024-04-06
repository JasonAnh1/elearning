package com.jason.elearning.controller;

import com.jason.elearning.configuration.Translator;
import com.jason.elearning.entity.Lesson;
import com.jason.elearning.entity.LessonProgress;
import com.jason.elearning.entity.request.AnswerSheetRequest;
import com.jason.elearning.entity.response.BaseResponse;
import com.jason.elearning.service.course.CoursePartService;
import com.jason.elearning.service.enroll.EnrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/")
@Transactional
public class LessonController extends BaseController {


    @Autowired
    private CoursePartService coursePartService;
    @Autowired
    private EnrollService enrollService;
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
    @PostMapping("v1/update-video-progress")
    public ResponseEntity<?> updateVideoProgress(@Valid @RequestBody final LessonProgress request) {
        try {
            if(request == null ||
                    request.getVideoProgress() == null||
                    request.getLessonId() == null) {
                throw new Exception(Translator.toLocale("required_fields"));
            }
            enrollService.updateVideoProgress(request.getVideoProgress(), request.getLessonId());
            return ResponseEntity.ok("success");
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @GetMapping("v1/get-video-progress")
    public ResponseEntity<?> getVideoProgress(@Valid @RequestParam Long lessonId) {
        try {
            if(lessonId == null ) {
                throw new Exception(Translator.toLocale("required_fields"));
            }

            return ResponseEntity.ok(enrollService.loadVideoProgress(lessonId));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @PostMapping("v1/answer-quizzes")
    public ResponseEntity<?> answerQuizzes(@Valid @RequestBody AnswerSheetRequest request) {
        try {
            if(request.getLessonId() == 0 ) {
                throw new Exception(Translator.toLocale("required_fields"));
            }

            return ResponseEntity.ok(coursePartService.checkAnswer(request));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
        @GetMapping("v1/unlock-lesson")
    public ResponseEntity<?> unlockLesson(@Valid @RequestParam Long request) {
        try {
            if(request == 0 ) {
                throw new Exception(Translator.toLocale("required_fields"));
            }

            return ResponseEntity.ok(coursePartService.unlockLesson(request));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @GetMapping("v1/delete-lesson")
    public ResponseEntity<?> deleteLesson(@Valid @RequestParam Long request) {
        try {
            if(request == 0 ) {
                throw new Exception(Translator.toLocale("required_fields"));
            }
            coursePartService.deleteLesson(request);
            return ResponseEntity.ok("success");
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
}
