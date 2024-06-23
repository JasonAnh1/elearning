package com.jason.elearning.repository.article;

import com.jason.elearning.entity.Article;
import com.jason.elearning.entity.QArticle;
import com.jason.elearning.repository.BaseRepository;
import com.querydsl.core.BooleanBuilder;

import java.util.List;

public class ArticleRepositoryImpl extends BaseRepository implements ArticleRepositoryCustom{
    @Override
    public List<Article> listArticle(int page, Long ownerId) {
        QArticle qArticle = QArticle.article;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qArticle.deleted.eq(false));
        if (ownerId != 0){
            builder.and(qArticle.userId.eq(ownerId));
        }
        return query()
                .from(qArticle)
                .select(qArticle)
                .where(builder)
                .offset((long) page * 4)
                .limit(4)
                .fetch();
    }

    @Override
    public Long countListArticle(Long ownerId) {
        QArticle qArticle = QArticle.article;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qArticle.deleted.eq(false));
        if (ownerId != 0){
            builder.and(qArticle.userId.eq(ownerId));
        }
        return query()
                .from(qArticle)
                .select(qArticle)
                .where(builder)
                .fetchCount();
    }
}
