package com.jason.elearning.controller;

import com.jason.elearning.configuration.Translator;
import com.jason.elearning.entity.CourseComment;
import com.jason.elearning.entity.LessonComment;
import com.jason.elearning.entity.request.CourseCommentRequest;
import com.jason.elearning.entity.response.BaseResponse;
import com.jason.elearning.service.comment.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/")
@Transactional
public class CommentController {
    @Autowired
    private CommentService commentService;



    @PostMapping("v1/create-lesson-comment")
        public ResponseEntity<?> createLessonComment(@Valid @RequestBody final LessonComment request) {
        try {
            if(request == null
            || request.getContent() == null) {
                throw new Exception(Translator.toLocale("required_fields"));
            }
            commentService.addLessonComment(request);

            return ResponseEntity.ok("success");
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }


    @GetMapping("v1/list-lesson-comment")
    public ResponseEntity<?> listLessonComment(@Valid @RequestParam final Long request) {
        try {
            if(request == null) {
                throw new Exception(Translator.toLocale("required_fields"));
            }
            ;
            return ResponseEntity.ok(commentService.listLessonComment(request));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }


    @PostMapping("v1/update-lesson-comment")
    public ResponseEntity<?> updateLessonComment(@Valid @RequestBody final LessonComment request) {
        try {
            if(request == null
                    || request.getContent() == null) {
                throw new Exception(Translator.toLocale("required_fields"));
            }
            commentService.updateLessonComment(request);
            return ResponseEntity.ok("success");
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @PostMapping("v1/delete-lesson-comment")
    public ResponseEntity<?> deleteLessonComment(@Valid @RequestParam final Long request) {
        try {
            if(request == null) {
                throw new Exception(Translator.toLocale("required_fields"));
            }
            commentService.deleteLessonComment(request);
            return ResponseEntity.ok("success");
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }

    // course comment

    @PostMapping("v1/update-course-comment")
    public ResponseEntity<?> updateCourseComment(@Valid @RequestBody final CourseComment request) {
        try {
            if(request == null
                    || request.getContent() == null) {
                throw new Exception(Translator.toLocale("required_fields"));
            }
            commentService.updateCourseComment(request);
            return ResponseEntity.ok("success");
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @PostMapping("v1/delete-course-comment")
    public ResponseEntity<?> deleteCourseComment(@Valid @RequestParam final Long request) {
        try {
            if(request == null) {
                throw new Exception(Translator.toLocale("required_fields"));
            }
            commentService.deleteCourseComment(request);
            return ResponseEntity.ok("success");
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }

    @PostMapping("v1/create-course-comment")
    public ResponseEntity<?> createCourseComment(@Valid @RequestBody final CourseCommentRequest request) {
        try {
            if(request == null
                    || request.getContent() == null) {
                throw new Exception(Translator.toLocale("required_fields"));
            }
            commentService.addCourseComment(request);
            return ResponseEntity.ok("success");
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }


    @GetMapping("v1/publish/list-course-comment")
    public ResponseEntity<?> listCourseComment(@Valid @RequestParam final Long request) {
        try {
            if(request == null) {
                throw new Exception(Translator.toLocale("required_fields"));
            }
            ;
            return ResponseEntity.ok(commentService.listCourseComment(request));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @GetMapping("v1/publish/list-most-like-comment")
    public ResponseEntity<?> listMostCourseComment(@Valid @RequestParam final Long request) {
        try {

            ;
            return ResponseEntity.ok(commentService.getMostLikeCourseComment(request));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @GetMapping("v1/like-course-comment")
    public ResponseEntity<?> likeCourseComment(@Valid @RequestParam final Long request) {
        try {
            if(request == 0 ) {
                throw new Exception(Translator.toLocale("required_fields"));
            }
            commentService.likeCourseComment(request);
            return ResponseEntity.ok("success");
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
}
