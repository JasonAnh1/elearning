package com.jason.elearning.service.article;

import com.jason.elearning.configuration.Translator;
import com.jason.elearning.entity.Article;
import com.jason.elearning.entity.User;
import com.jason.elearning.entity.constants.RoleName;
import com.jason.elearning.repository.article.ArticleRepository;
import com.jason.elearning.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ArticleServiceImpl extends BaseService implements ArticleService{
    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public Article postArticle(Article request) throws Exception {
        User user = getUser();
        if (user == null || user.getRoles().get(0).getName() == RoleName.ROLE_LEARNER) {
            throw new Exception(Translator.toLocale("access_denied"));
        }
        request.setUserId(user.getId());
        return articleRepository.save(request);
    }

    @Override
    public List<Article> listArticle() throws Exception {
        return articleRepository.findAll();
    }

    @Override
    public Article updateArticle(Article request) throws Exception {
        User user = getUser();
        if (user == null || user.getRoles().get(0).getName() == RoleName.ROLE_LEARNER) {
            throw new Exception(Translator.toLocale("access_denied"));
        }
        Article article = articleRepository.findById(request.getId()).orElseThrow(() -> new Exception("can not find article"));
        if(request.getDescription() != null){
            article.setDescription(request.getDescription());
        }
        if(request.getContent() != null){
            article.setContent(request.getContent());
        }
        if(request.getMedia() != null)
        {
            article.setMediaId(request.getMediaId());
        }
        if(request.getTitle() != null)
        {
            article.setTitle(request.getTitle());
        }

        return articleRepository.save(article);
    }

    @Override
    public Article getArticle(Long id) throws Exception {
        return articleRepository.findById(id).orElseThrow(() -> new Exception("can not find article"));
    }

    @Override
    public String deleteArticle(Long id) throws Exception {

        Article article = articleRepository.findById(id).orElseThrow(() -> new Exception("can not find article"));

        articleRepository.delete(article);

        return "Success";
    }
}
