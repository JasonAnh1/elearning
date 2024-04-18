package com.jason.elearning.repository.book;

import com.jason.elearning.entity.Book;
import com.jason.elearning.entity.QBook;
import com.jason.elearning.entity.constants.BookStatus;
import com.jason.elearning.repository.BaseRepository;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.jason.elearning.util.Util.PAGE_SIZE;

public class BookRepositoryImpl extends BaseRepository implements BookRepositoryCustom{
    @Override
    public List<Book> getBook(int page, String title, Long categoryId, Long authorId, String authorName, BookStatus status, Long startPrice, Long endPrice, Long userId) {
        QBook qBook = QBook.book;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qBook.deleted.eq(false));
        if(authorId != 0){
            builder.and(qBook.authorId.eq(authorId));
        }
        if(StringUtils.isNotBlank(title)){
            builder.and(qBook.title.like("%"+ title + "%"));
        }
        if(categoryId != 0){
            builder.and(qBook.categoryId.eq(categoryId));
        }
        if(status != null){
            builder.and(qBook.status.eq(status));
        }
        if (StringUtils.isNotEmpty(authorName)){
            builder.and(qBook.author.name.like("%"+ authorName + "%"));
        }
        if(startPrice != 0){
            builder.and(qBook.priceSale.goe(startPrice));
        }
        if (endPrice != 0){
            builder.and(qBook.priceSale.lt(endPrice));
        }

//        List<Enroll> enrollments = query()
//                .select(qEnroll)
//                .from(qEnroll)
//                .where(qEnroll.userId.eq(userId))
//                .fetch();

//        return query().from(qCourse)
//                .leftJoin(qEnroll).on(qCourse.id.eq(qEnroll.courseId))
//                .where(builder)
//                .groupBy(qCourse.id)
//                .select(qCourse, qEnroll.count())
//                .offset((long) page * PAGE_SIZE)
//                .limit(PAGE_SIZE)
//                .orderBy(qCourse.id.desc())
//                .fetch()
//                .stream()
//                .map(tuple -> {
//                    Course course = tuple.get(qCourse);
//                    Long learnerNumber = tuple.get(qEnroll.count());
//                    Objects.requireNonNull(course).setLearnerNumber(learnerNumber);
//                    course.setIsEnrolled(
//                            enrollments.stream().anyMatch(e -> e.getCourseId() == course.getId())
//                    );
//                    return course;
//                })
//                .collect(Collectors.toList());
        return query()
                .select(qBook)
                .from(qBook)
                .where(builder)
                .offset((long) page * PAGE_SIZE)
                .limit(PAGE_SIZE)
                .fetch();
    }

    @Override
    public Long getReadingNumber(Long courseId, Long learnerId) {
        return null;
    }

    @Override
    public List<Book> listForUserReading(long categoryId, String status) {
        return null;
    }

    @Override
    public Long countGetBook(String title, Long categoryId, Long authorId, String authorName, BookStatus status, Long startPrice, Long endPrice) {
        QBook qBook = QBook.book;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qBook.deleted.eq(false));
        if(authorId != 0){
            builder.and(qBook.authorId.eq(authorId));
        }
        if(StringUtils.isNotBlank(title)){
            builder.and(qBook.title.like("%"+ title + "%"));
        }
        if(categoryId != 0){
            builder.and(qBook.categoryId.eq(categoryId));
        }
        if(status != null){
            builder.and(qBook.status.eq(status));
        }
        if (StringUtils.isNotEmpty(authorName)){
            builder.and(qBook.author.name.like("%"+ authorName + "%"));
        }
        if(startPrice != 0){
            builder.and(qBook.priceSale.goe(startPrice));
        }
        if (endPrice != 0){
            builder.and(qBook.priceSale.lt(endPrice));
        }

//        List<Enroll> enrollments = query()
//                .select(qEnroll)
//                .from(qEnroll)
//                .where(qEnroll.userId.eq(userId))
//                .fetch();

//        return query().from(qCourse)
//                .leftJoin(qEnroll).on(qCourse.id.eq(qEnroll.courseId))
//                .where(builder)
//                .groupBy(qCourse.id)
//                .select(qCourse, qEnroll.count())
//                .offset((long) page * PAGE_SIZE)
//                .limit(PAGE_SIZE)
//                .orderBy(qCourse.id.desc())
//                .fetch()
//                .stream()
//                .map(tuple -> {
//                    Course course = tuple.get(qCourse);
//                    Long learnerNumber = tuple.get(qEnroll.count());
//                    Objects.requireNonNull(course).setLearnerNumber(learnerNumber);
//                    course.setIsEnrolled(
//                            enrollments.stream().anyMatch(e -> e.getCourseId() == course.getId())
//                    );
//                    return course;
//                })
//                .collect(Collectors.toList());
        return query()
                .select(qBook)
                .from(qBook)
                .where(builder)
                .limit(PAGE_SIZE)
                .fetchCount();
    }
}
