package com.jason.elearning.repository.course;

import com.jason.elearning.entity.Course;
import com.jason.elearning.entity.Enroll;
import com.jason.elearning.entity.QCourse;
import com.jason.elearning.entity.QEnroll;
import com.jason.elearning.entity.constants.CourseStatus;
import com.jason.elearning.repository.BaseRepository;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.jason.elearning.util.Util.PAGE_SIZE;

public class CourseRepositoryImpl extends BaseRepository implements CourseRepositoryCustom{
    @Override
    public List<Course> getCourse(int page, String title, Long categoryId, Long authorId, String authorName, CourseStatus status, Long startPrice, Long endPrice, Long userId) {
        QCourse qCourse = QCourse.course;
        QEnroll qEnroll = QEnroll.enroll;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qCourse.deleted.eq(false));
        if(authorId != 0){
            builder.and(qCourse.authorId.eq(authorId));
        }
        if(StringUtils.isNotBlank(title)){
            builder.and(qCourse.title.like("%"+ title + "%"));
        }
        if(categoryId != 0){
            builder.and(qCourse.categoryId.eq(categoryId));
        }
        if(status != null){
            builder.and(qCourse.status.eq(status));
        }
        if (StringUtils.isNotEmpty(authorName)){
            builder.and(qCourse.author.name.like("%"+ authorName + "%"));
        }
        if(startPrice != 0){
            builder.and(qCourse.priceSale.goe(startPrice));
        }
        if (endPrice != 0){
            builder.and(qCourse.priceSale.lt(endPrice));
        }

        List<Enroll> enrollments = query()
                .select(qEnroll)
                .from(qEnroll)
                .where(qEnroll.userId.eq(userId))
                .fetch();

        return query().from(qCourse)
                .leftJoin(qEnroll).on(qCourse.id.eq(qEnroll.courseId))
                .where(builder)
                .groupBy(qCourse.id)
                .select(qCourse, qEnroll.count())
                .offset((long) page * PAGE_SIZE)
                .limit(PAGE_SIZE)
                .orderBy(qCourse.id.desc())
                .fetch()
                .stream()
                .map(tuple -> {
                    Course course = tuple.get(qCourse);
                    Long learnerNumber = tuple.get(qEnroll.count());
                    Objects.requireNonNull(course).setLearnerNumber(learnerNumber);
                    course.setIsEnrolled(
                            enrollments.stream().anyMatch(e -> e.getCourseId() == course.getId())
                    );
                    return course;
                })
                .collect(Collectors.toList());
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
    public Long countGetCourse(String title, Long categoryId, Long authorId, String authorName, CourseStatus status, Long startPrice, Long endPrice) {
        QCourse qCourse = QCourse.course;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qCourse.deleted.eq(false));
        if(StringUtils.isNotBlank(title)){
            builder.and(qCourse.title.like("%"+ title + "%"));
        }
        if(categoryId != 0){
            builder.and(qCourse.categoryId.eq(categoryId));
        }
        if(status != null){
            builder.and(qCourse.status.eq(status));
        }
        if (StringUtils.isNotEmpty(authorName)){
            builder.and(qCourse.author.name.like("%"+ authorName + "%"));
        }
        if(startPrice != 0){
            builder.and(qCourse.priceSale.goe(startPrice));
        }
        if (endPrice != 0){
            builder.and(qCourse.priceSale.lt(endPrice));
        }
        if(authorId != 0){
            builder.and(qCourse.authorId.eq(authorId));
        }

        return   query().from(qCourse)
                .where(builder)
                .select(qCourse)
                .orderBy(qCourse.id.desc()).fetchCount();
    }
}
