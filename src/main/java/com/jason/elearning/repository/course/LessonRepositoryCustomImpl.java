package com.jason.elearning.repository.course;

import com.jason.elearning.entity.Lesson;
import com.jason.elearning.entity.LessonProgress;
import com.jason.elearning.entity.QLesson;
import com.jason.elearning.entity.QLessonProgress;
import com.jason.elearning.repository.BaseRepository;
import com.querydsl.core.BooleanBuilder;

import java.util.List;

public class LessonRepositoryCustomImpl extends BaseRepository implements LessonRepositoryCustom {

    @Override
    public List<Lesson> listLessonOrderByDateCreate(Long courseSectionId) {

        QLesson qLesson = QLesson.lesson;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qLesson.coursePartId.eq(courseSectionId));

        return  query().from(qLesson)
                .where(builder)
                .select(qLesson)
                .orderBy(qLesson.id.asc())
                .fetch();
    }

    @Override
    public List<LessonProgress> listLearningLessonProgress(Long userId, List<Long> lessonIds) {
        QLessonProgress qLessonProgress = QLessonProgress.lessonProgress;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qLessonProgress.userId.eq(userId));
        builder.and(qLessonProgress.lessonId.in(lessonIds));

        return  query().from(qLessonProgress)
                .where(builder)
                .select(qLessonProgress)
                .fetch();
    }
}
