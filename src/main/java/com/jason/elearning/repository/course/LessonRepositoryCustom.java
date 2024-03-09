package com.jason.elearning.repository.course;

import com.jason.elearning.entity.Lesson;

import java.util.List;

public interface LessonRepositoryCustom {
    List<Lesson> listLessonOrderByDateCreate(Long courseSectionId);
}
