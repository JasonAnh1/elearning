package com.jason.elearning.service.article;

import com.jason.elearning.entity.Article;

import java.util.List;

public interface ArticleService {
    Article postArticle(Article request) throws Exception;
    Article updateArticle(Article request) throws Exception;
    Article getArticle(Long id) throws Exception;
    String deleteArticle(Long id) throws Exception;
    List<Article> listArticle(int page, Long ownerId);
    Long countListArticle(Long ownerId);
}
