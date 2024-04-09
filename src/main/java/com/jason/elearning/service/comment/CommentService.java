package com.jason.elearning.service.comment;

import com.jason.elearning.entity.CourseComment;
import com.jason.elearning.entity.LessonComment;
import com.jason.elearning.entity.request.CourseCommentRequest;

import java.util.List;

public interface CommentService {
    void addLessonComment(LessonComment request) throws Exception;
    List<LessonComment> listLessonComment (Long lessonId) throws Exception;
    void deleteLessonComment(Long commentId);
    void updateLessonComment(LessonComment request) throws Exception;

    void updateCourseComment(CourseComment request);

    void deleteCourseComment(Long request);

    void addCourseComment(CourseCommentRequest request) throws Exception;

    List<CourseComment> listCourseComment(Long request);
    void likeCourseComment(Long commentId) throws Exception;
    CourseComment getMostLikeCourseComment(Long courseId);
}
