package com.jason.elearning.service.course;

import com.jason.elearning.entity.CoursePart;
import com.jason.elearning.entity.Lesson;
import com.jason.elearning.repository.course.CoursePartRepository;
import com.jason.elearning.repository.course.LessonRepository;
import com.jason.elearning.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoursePartServiceImpl extends BaseService implements CoursePartService{
    @Autowired
    private CoursePartRepository coursePartRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Override
    public CoursePart addSection(CoursePart coursePart) {


        return coursePartRepository.save(coursePart);
    }

    @Override
    public List<CoursePart> listCourseSession(long courseId) {

        return  coursePartRepository.getAllCourseSession(courseId);
    }

    @Override
    public Lesson addLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }
}
