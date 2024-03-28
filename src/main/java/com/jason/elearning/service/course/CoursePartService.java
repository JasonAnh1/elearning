package com.jason.elearning.service.course;

import com.jason.elearning.entity.CoursePart;
import com.jason.elearning.entity.Lesson;
import com.jason.elearning.entity.Quizz;
import com.jason.elearning.entity.request.QuizzesRequest;
import com.jason.elearning.entity.request.UpdateQuestionRequest;

import java.util.List;

public interface CoursePartService {
    CoursePart addSection(CoursePart coursePart);
    CoursePart updateSection(CoursePart coursePart) throws Exception;
    List<CoursePart> listCourseSession(long id);
    Lesson addLesson(Lesson lesson);
    Lesson getLessonById(long id) throws Exception;
    List<Quizz> addQuizzes(QuizzesRequest request);
    List<Quizz> listQuizzes(Long lessonId);
    List<Quizz> updateQuizzes(List<UpdateQuestionRequest> requests) throws Exception;
    Lesson updateLesson(Lesson lesson) throws Exception;
    List<CoursePart> listLearningLesson(long id) throws Exception;
}

