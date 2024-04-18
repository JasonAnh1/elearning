package com.jason.elearning.repository.book;

import com.jason.elearning.entity.Book;
import com.jason.elearning.entity.constants.BookStatus;

import java.util.List;

public interface BookRepositoryCustom {

    List<Book> getBook(int page, String title, Long categoryId, Long authorId, String authorName, BookStatus status, Long startPrice, Long endPrice, Long userId);
    Long getReadingNumber(Long courseId,Long learnerId);
    List<Book> listForUserReading(long categoryId,String status);
    Long countGetBook( String title,Long categoryId,Long authorId, String authorName, BookStatus status, Long startPrice, Long endPrice);
}
