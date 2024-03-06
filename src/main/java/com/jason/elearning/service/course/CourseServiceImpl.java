package com.jason.elearning.service.course;

import com.jason.elearning.configuration.Translator;
import com.jason.elearning.entity.Course;
import com.jason.elearning.entity.CourseCategory;
import com.jason.elearning.entity.User;
import com.jason.elearning.entity.constants.RoleName;
import com.jason.elearning.repository.course.CourseRepository;
import com.jason.elearning.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl extends BaseService implements CourseService{

    @Autowired
    private CourseRepository courseRepository;


    @Override
    public Course creatCourse(Course course) throws Exception {
        User user = getUser();
        if (user ==null || user.getRoles().get(0).getName() == RoleName.ROLE_LEARNER) {
            throw new Exception(Translator.toLocale("access_denied"));
        }
        course.setAuthorId(user.getId());
        return courseRepository.save(course);
    }

    @Override
    public List<Course> listCourse(int page,Long categoryId,String title,Long authorId) throws Exception{



        return courseRepository.getCourse(page,title,categoryId,authorId);
    }

    @Override
    public Course getCourseById(Long courseId) throws Exception {
        return courseRepository.findById(courseId).orElseThrow(()-> new Exception("can not find course"));
    }

}
