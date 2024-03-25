package com.jason.elearning.service.course;

import com.jason.elearning.configuration.Translator;
import com.jason.elearning.entity.Course;
import com.jason.elearning.entity.CourseCategory;
import com.jason.elearning.entity.User;
import com.jason.elearning.entity.constants.CourseStatus;
import com.jason.elearning.entity.constants.RoleName;
import com.jason.elearning.repository.course.CourseCategoryRepository;
import com.jason.elearning.repository.course.CourseRepository;
import com.jason.elearning.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl extends BaseService implements CourseService{

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseCategoryRepository courseCategoryRepository;

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

        return courseRepository.getCourse(page,title,categoryId,authorId,authorName,status,startPrice,endPrice, user == null ? -1 : user.getId());
    }

    @Override
    public Long countListCourse(Long categoryId, String title, Long authorId,String authorName, CourseStatus status,Long startPrice,Long endPrice) throws Exception {
        return courseRepository.countGetCourse(title,categoryId,authorId,authorName,status,startPrice,endPrice);
    }

    @Override
    public Course getCourseById(Long courseId) throws Exception {
        return courseRepository.findById(courseId).orElseThrow(()-> new Exception("can not find course"));
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

}
