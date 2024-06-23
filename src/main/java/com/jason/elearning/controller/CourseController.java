package com.jason.elearning.controller;

import com.jason.elearning.configuration.Translator;
import com.jason.elearning.entity.Course;
import com.jason.elearning.entity.CoursePart;
import com.jason.elearning.entity.Lesson;
import com.jason.elearning.entity.constants.CourseLevel;
import com.jason.elearning.entity.constants.CourseStatus;
import com.jason.elearning.entity.request.QuizzesRequest;
import com.jason.elearning.entity.request.WrappUpdateQuizzLesson;
import com.jason.elearning.entity.response.BaseResponse;
import com.jason.elearning.service.course.CategoryService;
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
    private CategoryService categoryService;
    @Autowired
    private CoursePartService coursePartService;
    @PostMapping("v1/create-quizzes")
    public ResponseEntity<?> createQuizz(@Valid @RequestBody final QuizzesRequest request) {
        try {
            if(request == null) {
                throw new Exception(Translator.toLocale("required_fields"));
            }
            return ResponseEntity.ok( coursePartService.addQuizzes(request));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
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
                    || request.getPosition() == 0
                    ){
                throw new Exception(Translator.toLocale("required_fields"));
            }
            return ResponseEntity.ok( coursePartService.addLesson(request));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @PostMapping("v1/update-course")
    public ResponseEntity updateLesson(@Valid @RequestBody final Course request) {
        try {
            if(request == null || request.getId() ==0
            ){
                throw new Exception(Translator.toLocale("required_fields"));
            }
            return ResponseEntity.ok( courseService.updateCourse(request));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @PostMapping("v1/update-section")
    public ResponseEntity<?> updateCourseSection(@Valid @RequestBody final CoursePart request) {
        try {
            if(request == null) {
                throw new Exception(Translator.toLocale("required_fields"));
            }
            return ResponseEntity.ok( coursePartService.updateSection(request));
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
    @GetMapping("v1/publish/get-lesson")
    public ResponseEntity<?> getLessonById(@RequestParam(required = false) Long lessonId) {
        try {

            return ResponseEntity.ok( coursePartService.getLessonById(lessonId));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @GetMapping("v1/get-quizzes")
    public ResponseEntity<?> getQuizzes(@RequestParam(required = false) Long lessonId) {
        try {

            return ResponseEntity.ok( coursePartService.listQuizzes(lessonId));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @GetMapping("v1/publish/list-course")
    public ResponseEntity<?> listAllCourse(@RequestParam final int page,
                                           @RequestParam(required = false) String title,
                                           @RequestParam(required = false) Long categoryId,
                                           @RequestParam(required = false) Long authorId,
                                           @RequestParam(required = false) CourseLevel level,
                                           @RequestParam(required = false) String authorName,
                                           @RequestParam(required = false) CourseStatus status,
                                           @RequestParam(required = false) Long startPrice,
                                           @RequestParam(required = false) Long endPrice) {
        try {

            return ResponseEntity.ok(new BaseResponse("Success", courseService.listCourse(page, categoryId, title, authorId,authorName,status,startPrice,endPrice,level),courseService.countListCourse(categoryId, title, authorId,authorName,status,startPrice,endPrice,level)) );
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
    @GetMapping("v1/list-learning-lesson")
        public ResponseEntity<?> listLearningLesson(@RequestParam final long courseId) {
        try {

            return ResponseEntity.ok( coursePartService.listLearningLesson(courseId));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @GetMapping("v1/promote-course")
    public ResponseEntity<?> promoteCourse(@RequestParam final Long courseId) {
        try {
            if(courseId == null) {
                throw new Exception(Translator.toLocale("required_fields"));
            }
            return ResponseEntity.ok( courseService.promoteCourse(courseId));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }

    @PostMapping("v1/update-quizzes")
    public ResponseEntity<?> updateQuizzes(@RequestBody  WrappUpdateQuizzLesson request) {
        try {
            if(request == null) {
                throw new Exception(Translator.toLocale("required_fields"));
            }
            return ResponseEntity.ok( coursePartService.updateQuizzes(request.getQuestions()));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }

    @GetMapping("v1/publish/list-all-course")
    public ResponseEntity<?> listAllCourses (@RequestParam(required = false) String title) {
        try {

            return ResponseEntity.ok(new BaseResponse("Success", courseService.listAllCourse( title)));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
}
