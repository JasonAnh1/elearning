package com.jason.elearning.repository.course;

import com.jason.elearning.entity.CoursePart;
import com.jason.elearning.entity.QCoursePart;
import com.jason.elearning.repository.BaseRepository;
import com.querydsl.core.BooleanBuilder;

import java.util.List;

import static com.jason.elearning.util.Util.PAGE_SIZE;

public class CoursePartRepositoryImpl extends BaseRepository implements CoursePartRepositoryCustom{
    @Override
    public List<CoursePart> getAllCourseSession(Long courseId) {
        QCoursePart qCoursePart = QCoursePart.coursePart;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qCoursePart.deleted.eq(false)).and(qCoursePart.courseId.eq(courseId));

        return query().from(qCoursePart)
                .where(builder)
                .select(qCoursePart)
                .orderBy(qCoursePart.partNumber.asc())
                .fetch();
    }
}
