package com.jason.elearning.service.book;

import com.jason.elearning.entity.Book;
import com.jason.elearning.entity.constants.BookStatus;

import java.util.List;

public interface BookService {
    List<Book> getLearnerBooks () throws Exception;
    void addBook(Book book) throws Exception;
    void updateBook(Book book) throws Exception;
    List<Book> listBook(int page, Long categoryId, String title, Long authorId, String authorName, BookStatus status, Long startPrice, Long endPrice) throws Exception;
    Book getBookById(long bookId) throws Exception;
    List<Book>listCourseForUserReading() throws Exception;
    List<Book> getBooks(String title,BookStatus status,Long categoryId,Long authorId,String authorName,Long StartPrice,Long endPrice,int page) throws Exception;
    String updateChapter(int chapter, long bookId) throws Exception;
    int currentChapter(long bookId) throws Exception;
    Boolean isBookPaid(long bookId) throws Exception;
}
