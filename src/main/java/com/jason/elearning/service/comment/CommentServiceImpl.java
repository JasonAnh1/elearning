package com.jason.elearning.service.comment;

import com.jason.elearning.configuration.Translator;
import com.jason.elearning.controller.NotificationController;
import com.jason.elearning.entity.*;
import com.jason.elearning.entity.request.CourseCommentRequest;
import com.jason.elearning.repository.comment.CourseCommentRepository;
import com.jason.elearning.repository.comment.LessonCommentRepository;
import com.jason.elearning.repository.course.CoursePartRepository;
import com.jason.elearning.repository.course.CourseRepository;
import com.jason.elearning.repository.course.LessonRepository;
import com.jason.elearning.repository.user.EnrollRepository;
import com.jason.elearning.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class CommentServiceImpl extends BaseService implements CommentService {

    @Autowired
    private LessonCommentRepository lessonCommentRepository;
    @Autowired
    private CourseCommentRepository courseCommentRepository;
    @Autowired
    private EnrollRepository enrollRepository;
    @Autowired
    private NotificationController notificationController;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private CoursePartRepository coursePartRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Override
    public void addLessonComment(LessonComment request) throws Exception {
        User user = getUser();
        if (user ==null) {
            throw new Exception(Translator.toLocale("access_denied"));
        }

        request.setUserId(user.getId());

        Long ownerId = getLessonOwnerId(request.getLessonId());
        String notificationMessage = user.getName() + " liked your post.";
        notificationController.sendNotification(ownerId, notificationMessage);
        lessonCommentRepository.save(request);
    }
    public Long getLessonOwnerId(Long lessonId) throws Exception {
        Long coursePartId  = lessonRepository.findById(lessonId).orElseThrow(()-> new Exception("can not find lesson ")).getCoursePartId();
        Long courseId = coursePartRepository.findById(coursePartId).orElseThrow(()-> new Exception("can not find cp ")).getCourseId();
        User user = courseRepository.findById(courseId).orElseThrow(()-> new Exception("can not find course ")).getAuthor();
        return user.getId();
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

    @Override
    public void updateCourseComment(CourseComment request) {

    }

    @Override
    public void deleteCourseComment(Long request) {

    }

    @Override
    public void addCourseComment(CourseCommentRequest request) throws Exception{
        User user = getUser();
        if (user ==null) {
            throw new Exception(Translator.toLocale("access_denied"));
        }
        Enroll enroll = enrollRepository.findFirstByUserIdAndCourseId(user.getId(), request.getCourseId());
        if (enroll ==null) {
            throw new Exception(Translator.toLocale("access_denied"));
        }
        if (courseCommentRepository.existsByEnrollId(enroll.getId())){
            throw new Exception(Translator.toLocale("You already comment"));
        }
        CourseComment courseComment = new CourseComment();
        courseComment.setContent(request.getContent());
        courseComment.setEnrollId(enroll.getId());
        courseComment.setRate(request.getRate());

        courseCommentRepository.save(courseComment);
    }

    @Override
    public List<CourseComment> listCourseComment(Long request) {

        List<CourseComment> listCourseComment = courseCommentRepository.listCommentByCourseId(request);
        listCourseComment.forEach(cm -> cm.setUser(userRepository.findUserById(cm.getEnroll().getUserId())));

        return courseCommentRepository.listCommentByCourseId(request);
    }

    @Override
    public void likeCourseComment(Long commentId) throws Exception {
        CourseComment courseComment = courseCommentRepository.findById(commentId).orElseThrow(()-> new Exception("can not find course comment"));
        courseComment.setLiked(courseComment.getLiked()+1);
        courseCommentRepository.save(courseComment);
    }

    @Override
    public CourseComment getMostLikeCourseComment(Long courseId) {
        CourseComment courseComment =  courseCommentRepository.getMostLikedCourseComment(courseId);
        courseComment.setUser(userRepository.findUserById(courseComment.getEnroll().getUserId()));
        return courseComment;
    };

}
