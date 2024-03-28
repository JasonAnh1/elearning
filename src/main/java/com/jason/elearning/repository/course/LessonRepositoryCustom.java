package com.jason.elearning.repository.course;

import com.jason.elearning.entity.Lesson;
import com.jason.elearning.entity.LessonProgress;

import java.util.List;

public interface LessonRepositoryCustom {
    List<Lesson> listLessonOrderByDateCreate(Long courseSectionId);
    List<LessonProgress> listLearningLessonProgress(Long userId, List<Long> lessonIds);
}
