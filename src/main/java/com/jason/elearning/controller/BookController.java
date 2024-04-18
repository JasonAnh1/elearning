package com.jason.elearning.controller;

import com.jason.elearning.configuration.Translator;
import com.jason.elearning.entity.Book;
import com.jason.elearning.entity.response.BaseResponse;
import com.jason.elearning.service.book.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
                    || request.getPriceSale()==0) {
                throw new Exception(Translator.toLocale("required_fields"));
            }
            bookService.addBook(request);
            return ResponseEntity.ok("success");
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }


}
