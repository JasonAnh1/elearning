package com.jason.elearning.service.comment;

import com.jason.elearning.entity.LessonComment;

import java.util.List;

public interface LessonCommentService {
    void addLessonComment(LessonComment request) throws Exception;
    List<LessonComment> listLessonComment (Long lessonId) throws Exception;
    void deleteLessonComment(Long commentId);
    void updateLessonComment(LessonComment request) throws Exception;
}
