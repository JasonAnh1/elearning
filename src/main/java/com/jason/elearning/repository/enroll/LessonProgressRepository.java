package com.jason.elearning.repository.enroll;

import com.jason.elearning.entity.LessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonProgressRepository extends JpaRepository<LessonProgress,Long> {
    LessonProgress findFirstByUserIdAndLessonId(long userId,long lessonId);
    List<LessonProgress> findAllByLessonId(long lessonId);
    boolean existsByLessonIdAndUserId(Long lessonId,Long userId);
}
