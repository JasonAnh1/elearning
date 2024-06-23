package com.jason.elearning.controller;

import com.jason.elearning.configuration.Translator;
import com.jason.elearning.entity.Article;
import com.jason.elearning.entity.response.BaseResponse;
import com.jason.elearning.service.article.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/")
@Transactional
public class ArticleController extends BaseController{

    @Autowired
    private ArticleService articleService;

    @PostMapping("v1/post-article")
    public ResponseEntity<?> postArticle(@Valid @RequestBody final Article request) {
        try {
            if(request == null || request.getTitle() == null) {
                throw new Exception(Translator.toLocale("required_fields"));
            }
            return ResponseEntity.ok( articleService.postArticle(request));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @PostMapping("v1/update-article")
    public ResponseEntity<?> updateArticle(@Valid @RequestBody final Article request) {
        try {
            if(request == null || request.getTitle() == null) {
                throw new Exception(Translator.toLocale("required_fields"));
            }
            return ResponseEntity.ok( articleService.updateArticle(request));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }

    @GetMapping("v1/publish/get-articles")
    public ResponseEntity<?> getBooks(@RequestParam(required = false) final int page,
                                      @RequestParam(required = false) String title,
                                      @RequestParam(required = false) Long authorId,
                                      @RequestParam(required = false) Long ownerId
                                     ) {
        try {

            return ResponseEntity.ok( new BaseResponse("success",articleService.listArticle(page, ownerId),articleService.countListArticle(ownerId)));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @GetMapping("v1/publish/article")
    public ResponseEntity<?> getBook(@RequestParam(required = false)  final long articleId) {
        try {

            return ResponseEntity.ok( articleService.getArticle(articleId));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @GetMapping("v1/delete-article")
    public ResponseEntity<?> deleteArticle(@RequestParam(required = false)  final long articleId) {
        try {

            return ResponseEntity.ok( articleService.deleteArticle(articleId));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
}
