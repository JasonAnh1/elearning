package com.jason.elearning.service.book;

import com.jason.elearning.entity.Book;
import com.jason.elearning.entity.constants.BookStatus;

import java.util.List;

public interface BookService {
    void addBook(Book book);
    void updateBook(Book book) throws Exception;
    List<Book> listBook(int page, Long categoryId, String title, Long authorId, String authorName, BookStatus status, Long startPrice, Long endPrice) throws Exception;
    Book getBookById(long bookId) throws Exception;
    List<Book>listCourseForUserReading() throws Exception;
}
