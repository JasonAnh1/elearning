package com.jason.elearning.repository.comment;

import com.jason.elearning.entity.LessonComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonCommentRepository extends JpaRepository<LessonComment,Long> {
    List<LessonComment> findAllByLessonIdAndParentCommentIdIsNullOrderByUpdatedAtDesc(long lessonId);
    List<LessonComment> findAllByParentCommentId(long parentCommentId);
    List<LessonComment> findAllByLessonId(long lessonId);
}
