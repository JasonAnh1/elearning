package com.jason.elearning.service.comment;

import com.jason.elearning.configuration.Translator;
import com.jason.elearning.entity.LessonComment;
import com.jason.elearning.entity.User;
import com.jason.elearning.repository.comment.LessonCommentRepository;
import com.jason.elearning.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class LessonCommentServiceImpl extends BaseService implements LessonCommentService {

    @Autowired
    private LessonCommentRepository lessonCommentRepository;


    @Override
    public void addLessonComment(LessonComment request) throws Exception {
        User user = getUser();
        if (user ==null) {
            throw new Exception(Translator.toLocale("access_denied"));
        }

        request.setUserId(user.getId());
        lessonCommentRepository.save(request);
    }

    @Override
    public List<LessonComment> listLessonComment(Long lessonId) throws Exception {

        List<LessonComment> lessonCommentList = lessonCommentRepository.findAllByLessonIdAndParentCommentIdIsNullOrderByUpdatedAtDesc(lessonId);
        for(LessonComment ls : lessonCommentList){
            List<LessonComment> childCommentList = lessonCommentRepository.findAllByParentCommentId(ls.getId());
            ls.setChildrenComments(childCommentList);
        }

        return lessonCommentList;
    }

    @Override
    public void deleteLessonComment(Long commentId) {
        List<LessonComment> childCommentList = lessonCommentRepository.findAllByParentCommentId(commentId);
        if(childCommentList.size()>0){
            lessonCommentRepository.deleteAll(childCommentList);
        }
        lessonCommentRepository.deleteById(commentId);

    }

    @Override
    public void updateLessonComment(LessonComment request) throws Exception {

        LessonComment lessonComment = lessonCommentRepository.findById(request.getId()).orElseThrow(()-> new Exception("can not find lesson comment"));
        lessonComment.setContent(request.getContent());
        lessonCommentRepository.save(lessonComment);
    }
}
