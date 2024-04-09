package com.jason.elearning.repository.comment;

import com.jason.elearning.entity.CourseComment;

import java.util.List;

public interface CourseCommentRepositoryCustom {
    List<CourseComment> listCommentByCourseId(long courseId);
    CourseComment getMostLikedCourseComment(Long courseId);
}
