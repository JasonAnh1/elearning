package com.jason.elearning.repository.enroll;

import com.jason.elearning.entity.LessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonProgressRepository extends JpaRepository<LessonProgress,Long> {
    LessonProgress findFirstByUserIdAndLessonId(long userId,long lessonId);
}
