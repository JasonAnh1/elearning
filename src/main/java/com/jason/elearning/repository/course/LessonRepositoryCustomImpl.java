package com.jason.elearning.repository.course;

import com.jason.elearning.entity.Lesson;
import com.jason.elearning.entity.QLesson;
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
}
