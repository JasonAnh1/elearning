package com.jason.elearning.repository.comment;

import com.jason.elearning.entity.CourseComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseCommentRepository extends JpaRepository<CourseComment,Long>, CourseCommentRepositoryCustom {
    boolean existsByEnrollId(long enrollId);
}
