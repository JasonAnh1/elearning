package com.jason.elearning.controller;

import com.jason.elearning.configuration.Translator;
import com.jason.elearning.entity.Book;
import com.jason.elearning.entity.constants.BookStatus;
import com.jason.elearning.entity.response.BaseResponse;
import com.jason.elearning.service.book.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/")
@Transactional
public class BookController extends BaseController{
    @Autowired
    private BookService bookService;

    @PostMapping("v1/create-book")
    public ResponseEntity<?> createCourse(@Valid @RequestBody final Book request) {
        try {
            if(request == null
                    || request.getTitle() == null
                    || request.getCategoryId() == 0
                    || request.getPrice() ==0
                    || request.getPriceSale()==0
                    || request.getMediaId() == 0) {
                throw new Exception(Translator.toLocale("required_fields"));
            }
            bookService.addBook(request);
            return ResponseEntity.ok("success");
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }

    @GetMapping("v1/publish/get-books")
    public ResponseEntity<?> getBooks(@RequestParam final int page,
                                      @RequestParam(required = false) String title,
                                      @RequestParam(required = false) Long categoryId,
                                      @RequestParam(required = false) Long authorId,
                                      @RequestParam(required = false) String authorName,
                                      @RequestParam(required = false) BookStatus bookStatus,
                                      @RequestParam(required = false) Long startPrice,
                                      @RequestParam(required = false) Long endPrice) {
        try {

            return ResponseEntity.ok( bookService.getBooks(title,bookStatus,categoryId,authorId,authorName,startPrice,endPrice,page));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @GetMapping("v1/publish/learner-books")
    public ResponseEntity<?> getBooks() {
        try {

            return ResponseEntity.ok( bookService.getLearnerBooks());
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @GetMapping("v1/publish/book")
    public ResponseEntity<?> getBook(@RequestParam final int bookId) {
        try {

            return ResponseEntity.ok( bookService.getBookById(bookId));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @GetMapping("v1/update-chapter")
    public ResponseEntity<?> updateChapter(@RequestParam final int chapter,
                                           @RequestParam final long bookId) {
        try {

            return ResponseEntity.ok( bookService.updateChapter(chapter,bookId));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @GetMapping("v1/current-chapter")
    public ResponseEntity<?> currentChapter(
                                           @RequestParam final long bookId) {
        try {

            return ResponseEntity.ok( bookService.currentChapter(bookId));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }

    @GetMapping("v1/publish/user-book-check")
    public ResponseEntity<?> userBookCheck(
            @RequestParam final long bookId) {
        try {

            return ResponseEntity.ok( bookService.isBookPaid(bookId));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }


}
