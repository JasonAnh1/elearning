package com.jason.elearning.service.course;

import com.jason.elearning.entity.Choice;
import com.jason.elearning.entity.CoursePart;
import com.jason.elearning.entity.Lesson;
import com.jason.elearning.entity.Quizz;
import com.jason.elearning.entity.constants.QuizzType;
import com.jason.elearning.entity.request.QuizzRequest;
import com.jason.elearning.entity.request.QuizzesRequest;
import com.jason.elearning.repository.course.ChoiceRepository;
import com.jason.elearning.repository.course.CoursePartRepository;
import com.jason.elearning.repository.course.LessonRepository;
import com.jason.elearning.repository.course.QuizzRepository;
import com.jason.elearning.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoursePartServiceImpl extends BaseService implements CoursePartService{
    @Autowired
    private CoursePartRepository coursePartRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private QuizzRepository quizzRepository;
    @Autowired
    private ChoiceRepository choiceRepository;


    @Override
    public CoursePart addSection(CoursePart coursePart) {


        return coursePartRepository.save(coursePart);
    }

    @Override
    public List<CoursePart> listCourseSession(long courseId) {
        List<CoursePart> lstSections=  coursePartRepository.getAllCourseSession(courseId);
        for(CoursePart cp: lstSections){
            List<Lesson> ls = lessonRepository.listLessonOrderByDateCreate(cp.getId());
            if(ls != null){
                cp.setLessons(ls);
            }
        }

        return  lstSections;
    }

    @Override
    public Lesson addLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    @Override
    public Lesson getLessonById(long id) throws Exception {
        return lessonRepository.findById(id).orElseThrow(()-> new Exception("can not find lesson"));
    }

    @Override
    public List<Quizz> addQuizzes(QuizzesRequest request) {
        List<Quizz> lstQuizz = new LinkedList<>();
        for(QuizzRequest qr: request.getQuizzList()){
            Quizz q = new Quizz();
            q.setLessonId(request.getLessonId());
            q.setQuestion(qr.getQQuestion());
            q.setAnswer(qr.getQAnswer());
            q.setType(qr.getQType());

            lstQuizz.add(q);
            long quizzId = quizzRepository.save(q).getId();
            if (qr.getQType().equals(QuizzType.MULTIPLECHOICE)){
                List<Choice> lstChoice = new LinkedList<>();
                Choice a = new Choice();
                a.setQuizzId(quizzId);
                a.setContent(qr.getQA());
                lstChoice.add(a);
                Choice b = new Choice();
                b.setQuizzId(quizzId);
                b.setContent(qr.getQB());
                lstChoice.add(b);
                Choice c = new Choice();
                c.setQuizzId(quizzId);
                c.setContent(qr.getQC());
                lstChoice.add(c);
                Choice d = new Choice();
                d.setQuizzId(quizzId);
                d.setContent(qr.getQD());
                lstChoice.add(d);
                choiceRepository.saveAll(lstChoice);
            }
        }
        return lstQuizz;
    }

    @Override
    public List<Quizz> listQuizzes(Long lessonId) {
        return quizzRepository.findAllByLessonId(lessonId);
    }

    

}
