package com.jason.elearning.repository.article;

import com.jason.elearning.entity.Article;

import java.util.List;

public interface ArticleRepositoryCustom {
   List<Article> listArticle(int page,Long ownerId);
   Long countListArticle(Long ownerId);
}
