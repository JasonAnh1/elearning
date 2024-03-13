package com.jason.elearning.repository.course;

import com.jason.elearning.entity.Course;
import com.jason.elearning.entity.QCourse;
import com.jason.elearning.entity.QEnroll;
import com.jason.elearning.repository.BaseRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.var;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.jason.elearning.util.Util.PAGE_SIZE;

public class CourseRepositoryImpl extends BaseRepository implements CourseRepositoryCustom{
    @Override
    public List<Course> getCourse(int page, String title, Long categoryId, Long authorId) {
        QCourse qCourse = QCourse.course;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qCourse.deleted.eq(false));
        if(StringUtils.isNotBlank(title)){
            builder.and(qCourse.title.like("%"+ title + "%"));
        }
        if(categoryId != null){
            builder.and(qCourse.categoryId.eq(categoryId));
        }
        if(authorId != null){
            builder.and(qCourse.authorId.eq(authorId));
        }
        var a= query().from(qCourse)
                .where(builder)
                .select(qCourse)
                .offset(page * PAGE_SIZE)
                .limit(PAGE_SIZE)
                .orderBy(qCourse.id.desc());
        return a.fetch();
    }

    @Override
    public Long getEnrollNumber(Long courseId, Long learnerId) {
        QEnroll qEnroll = QEnroll.enroll;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qEnroll.courseId.eq(courseId));
        builder.and(qEnroll.userId.eq(learnerId));

        return query()
                .from(qEnroll)
                .where(builder)
                .fetchCount();
    }

    @Override
    public Long countGetCourse(String title, Long categoryId, Long authorId) {
        QCourse qCourse = QCourse.course;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qCourse.deleted.eq(false));
        if(StringUtils.isNotBlank(title)){
            builder.and(qCourse.title.like("%"+ title + "%"));
        }
        if(categoryId != null){
            builder.and(qCourse.categoryId.eq(categoryId));
        }
        if(authorId != null){
            builder.and(qCourse.authorId.eq(authorId));
        }

        return   query().from(qCourse)
                .where(builder)
                .select(qCourse)
                .orderBy(qCourse.id.desc()).fetchCount();
    }
}
