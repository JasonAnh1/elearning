package com.jason.elearning.repository.comment;

import com.jason.elearning.entity.CourseComment;
import com.jason.elearning.entity.QCourseComment;
import com.jason.elearning.repository.BaseRepository;
import com.querydsl.core.BooleanBuilder;

import java.util.List;

public class CourseCommentRepositoryImpl extends BaseRepository implements CourseCommentRepositoryCustom{

    @Override
    public List<CourseComment> listCommentByCourseId(long courseId) {

        QCourseComment qCourseComment = QCourseComment.courseComment;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qCourseComment.enroll.courseId.eq(courseId));

        return query().from(qCourseComment)
                .where(builder)
                .select(qCourseComment)
                .fetch();

    }

    @Override
    public CourseComment getMostLikedCourseComment(Long courseId) {

        QCourseComment qCourseComment = QCourseComment.courseComment;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qCourseComment.enroll.courseId.eq(courseId));

        return query().from(qCourseComment)
                .select(qCourseComment)
                .where(builder)
                .orderBy(qCourseComment.liked.desc())
                .fetchFirst();
    }
}
