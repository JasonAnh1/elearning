package com.jason.elearning.service.course;

import com.jason.elearning.entity.CoursePart;
import com.jason.elearning.entity.Lesson;
import com.jason.elearning.entity.Quizz;
import com.jason.elearning.entity.request.QuizzesRequest;

import java.util.List;

public interface CoursePartService {
    CoursePart addSection(CoursePart coursePart);
    List<CoursePart> listCourseSession(long id);
    Lesson addLesson(Lesson lesson);
    Lesson getLessonById(long id) throws Exception;
    List<Quizz> addQuizzes(QuizzesRequest request);
    List<Quizz> listQuizzes(Long lessonId);
}

