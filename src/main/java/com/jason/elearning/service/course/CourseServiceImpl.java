package com.jason.elearning.service.course;

import com.jason.elearning.configuration.Translator;
import com.jason.elearning.entity.*;
import com.jason.elearning.entity.constants.CourseStatus;
import com.jason.elearning.entity.constants.RoleName;
import com.jason.elearning.repository.comment.CourseCommentRepository;
import com.jason.elearning.repository.course.CourseCategoryRepository;
import com.jason.elearning.repository.course.CoursePartRepository;
import com.jason.elearning.repository.course.CourseRepository;
import com.jason.elearning.repository.course.LessonRepository;
import com.jason.elearning.repository.user.EnrollRepository;
import com.jason.elearning.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl extends BaseService implements CourseService{

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseCategoryRepository courseCategoryRepository;
    @Autowired
    private EnrollRepository enrollRepository;
    @Autowired
    private CoursePartRepository coursePartRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private CourseCommentRepository courseCommentRepository;
    @Override
    public Course creatCourse(Course course) throws Exception {
        User user = getUser();
        if (user ==null || user.getRoles().get(0).getName() == RoleName.ROLE_LEARNER) {
            throw new Exception(Translator.toLocale("access_denied"));
        }
        course.setAuthorId(user.getId());
        course.setStatus(CourseStatus.PENDING);
        return courseRepository.save(course);
    }

    @Override
    public  List<Course> listCourse(int page, Long categoryId, String title, Long authorId, String authorName, CourseStatus status, Long startPrice, Long endPrice) throws Exception{

        User user = getUser();
        List<Course> lstCourse = courseRepository.getCourse(page,title,categoryId,authorId,authorName,status,startPrice,endPrice, user == null ? -1 : user.getId());
        calculateCourseRating(lstCourse);
        return lstCourse;
    }

    private void calculateCourseRating(List<Course> lstCourse) {
        for(Course course: lstCourse){
            List<Double> lstRate = courseCommentRepository.listCommentByCourseId(course.getId())
                                        .stream()
                                        .map(CourseComment::getRate).collect(Collectors.toList());
            if(lstRate.size() > 0){
                double sumRate = lstRate.stream().mapToDouble(Double::doubleValue).sum();
                double medRate = sumRate / lstRate.size();

                course.setRating(medRate);
            }
        }
    }


    @Override
    public Long countListCourse(Long categoryId, String title, Long authorId,String authorName, CourseStatus status,Long startPrice,Long endPrice) throws Exception {
        return courseRepository.countGetCourse(title,categoryId,authorId,authorName,status,startPrice,endPrice);
    }

    @Override
    public Course getCourseById(Long courseId) throws Exception {
        User user = getUser();
        Course course = courseRepository.findById(courseId)
                .orElseThrow(()-> new Exception("can not find course"));
        if(user!= null){
            Enroll enroll = enrollRepository.findFirstByUserIdAndCourseId(user.getId(),courseId);
            course.setIsEnrolled(enroll != null);
        }
        return course;
    }

    @Override
    public Course updateCourse(Course request) throws Exception {

        Course c = courseRepository.findById(request.getId()).orElseThrow(()-> new Exception("can not find course"));
        if(request.getCategoryId() != 0){
            CourseCategory cc = courseCategoryRepository.findById(request.getCategoryId()).orElseThrow(()-> new Exception("can not find course category"));
            c.setCategoryId(cc.getId());
        }
        if(request.getTitle()!=null){
            c.setTitle(request.getTitle());
        }
        if(request.getCourseContent()!=null){
            c.setCourseContent(request.getCourseContent());
        }
        if(request.getDescription()!=null){
            c.setDescription(request.getDescription());
        }
        if(request.getDetail()!=null){
            c.setDetail(request.getDetail());
        }
        if(request.getDescription()!=null){
            c.setDescription(request.getDescription());
        }
        if(request.getShortDes()!=null){
            c.setShortDes(request.getShortDes());
        }
        if(request.getRequirement()!=null){

            c.setRequirement(request.getRequirement());
        }
        if(request.getCategoryId() != 0){
            c.setCategoryId(request.getCategoryId());
        }
        if(request.getPrice() != 0){
            c.setPrice(request.getPrice());
        }
        if(request.getPriceSale() != 0){
            c.setPriceSale(request.getPriceSale());
        }
        if(request.getStatus() != null){
            c.setStatus(request.getStatus());
        }
        if(request.getMediaId() != null){
            c.setMediaId(request.getMediaId());
        }
        return courseRepository.save(c);
    }

    @Override
    public List<Course> listCourseForUserEnrolled() throws Exception {
        User user = getUser();
        if (user ==null ) {
            throw new Exception(Translator.toLocale("access_denied"));
        }
       List<Enroll> lstEnrolled = enrollRepository.findByUserId(user.getId());
        List<Course> lstCourse = lstEnrolled.stream().map(
                Enroll::getCourse
        ).collect(Collectors.toList());

        for(Course course: lstCourse ){
            setProgress(course,user.getId());
        }


        return lstCourse;
    }

    private void setProgress(Course course, long id) {
        List<Long> coursePartListId = coursePartRepository.findAllByCourseId(course.getId())
                .stream().map(CoursePart::getId).collect(Collectors.toList());

        List<Long> lessonList = lessonRepository.listLessonByListCoursePartId(coursePartListId)
                .stream().map(Lesson::getId).collect(Collectors.toList());

        List<LessonProgress> lessonProgresses = lessonRepository.listLearningLessonProgress(id,lessonList);
        int lessonDone = 0;
        for(LessonProgress lsProgress: lessonProgresses){
            if(lsProgress.getProgress() == null){
                lsProgress.setProgress(0.0);
            }
            if(lsProgress.getProgress() == 100){
                lessonDone++;
            }
        }
        double total = lessonProgresses.size();
        double courseProgress = lessonDone / total * 100;
        if(total == 0){
            course.setProgress(0);
        }else {
            course.setProgress(courseProgress);
        }

    }


}
