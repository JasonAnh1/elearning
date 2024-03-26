package com.jason.elearning.service.course;

import com.jason.elearning.entity.*;
import com.jason.elearning.entity.constants.LessonType;
import com.jason.elearning.entity.constants.QuizzType;
import com.jason.elearning.entity.request.QuizzRequest;
import com.jason.elearning.entity.request.QuizzesRequest;
import com.jason.elearning.entity.request.UpdateQuestionRequest;
import com.jason.elearning.repository.ImageRepository;
import com.jason.elearning.repository.course.ChoiceRepository;
import com.jason.elearning.repository.course.CoursePartRepository;
import com.jason.elearning.repository.course.LessonRepository;
import com.jason.elearning.repository.course.QuizzRepository;
import com.jason.elearning.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CoursePartServiceImpl extends BaseService implements CoursePartService {
    @Autowired
    private CoursePartRepository coursePartRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private QuizzRepository quizzRepository;
    @Autowired
    private ChoiceRepository choiceRepository;
    @Autowired
    private ImageRepository imageRepository;

    @Override
    public CoursePart addSection(CoursePart coursePart) {


        return coursePartRepository.save(coursePart);
    }

    @Override
    public CoursePart updateSection(CoursePart coursePart) throws Exception {
        CoursePart cp = coursePartRepository.findById(coursePart.getId()).orElseThrow(() -> new Exception("can not find section"));
        cp.setTitle(coursePart.getTitle());
        cp.setPartNumber(coursePart.getPartNumber());
        return coursePartRepository.save(cp);
    }

    @Override
    public List<CoursePart> listCourseSession(long courseId) {
        List<CoursePart> lstSections = coursePartRepository.getAllCourseSession(courseId);
        for (CoursePart cp : lstSections) {
            List<Lesson> ls = lessonRepository.listLessonOrderByDateCreate(cp.getId());
            if (ls != null) {
                cp.setLessons(ls);
            }
        }

        return lstSections;
    }

    @Override
    public Lesson addLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    @Override
    public Lesson getLessonById(long id) throws Exception {
        return lessonRepository.findById(id).orElseThrow(() -> new Exception("can not find lesson"));
    }

    @Override
    public List<Quizz> addQuizzes(QuizzesRequest request) {
        List<Quizz> lstQuizz = new LinkedList<>();
        for (QuizzRequest qr : request.getQuizzList()) {
            Quizz q = new Quizz();
            q.setLessonId(request.getLessonId());
            q.setQuestion(qr.getQQuestion());
            q.setAnswer(qr.getQAnswer());
            q.setType(qr.getQType());

            lstQuizz.add(q);
            long quizzId = quizzRepository.save(q).getId();
            if (qr.getQType().equals(QuizzType.MULTIPLECHOICE)) {
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

    @Override
    @Transactional
    public List<Quizz> updateQuizzes(List<UpdateQuestionRequest> requests) throws Exception {
        List<Long> removeQuestionIds = new ArrayList<>();
        List<Quizz> batchUpdateQuestion = new ArrayList<>();
        List<Choice> batchUpdateChoice = new ArrayList<>();
        List<Long> removeChoiceByQuizzIds = new ArrayList<>();
        List<Quizz> quizzUpdates = quizzRepository.findAllById(requests
                .stream()
                .filter(e -> e.getQId() != null && e.getRemoveFlag() == null)
                .map(UpdateQuestionRequest::getQId)
                .collect(Collectors.toList()));

        for (UpdateQuestionRequest question : requests) {
            if (question.getRemoveFlag() != null && question.getRemoveFlag()) {
                removeQuestionIds.add(question.getQId());

            } else if (question.getQId() == null) {
                Quizz newQuizz = new Quizz();
                newQuizz.setType(question.getQType());
                newQuizz.setQuestion(question.getQQuestion());
                newQuizz.setAnswer(question.getQAnswer());
                newQuizz.setLessonId(question.getLessonId());
                batchUpdateQuestion.add(newQuizz);
                if (newQuizz.getType() == QuizzType.MULTIPLECHOICE) {
                    Choice newChoiceA = new Choice();
                    newChoiceA.setContent(question.getQA().getContent());
                    newChoiceA.setQuizz(newQuizz);

                    Choice newChoiceB = new Choice();
                    newChoiceB.setContent(question.getQB().getContent());
                    newChoiceB.setQuizz(newQuizz);

                    Choice newChoiceC = new Choice();
                    newChoiceC.setContent(question.getQC().getContent());
                    newChoiceC.setQuizz(newQuizz);

                    Choice newChoiceD = new Choice();
                    newChoiceD.setContent(question.getQD().getContent());
                    newChoiceD.setQuizz(newQuizz);


                    batchUpdateChoice.add(newChoiceA);
                    batchUpdateChoice.add(newChoiceB);
                    batchUpdateChoice.add(newChoiceC);
                    batchUpdateChoice.add(newChoiceD);
                }
            } else {
                Quizz quizzUpdate = quizzUpdates.stream().filter(e -> e.getId() == question.getQId()).findFirst().orElseThrow(() -> new Exception("can not find quizz"));
                ;

                if (quizzUpdate.getType() == QuizzType.MULTIPLECHOICE && question.getQType() == QuizzType.FILLINTHEBLANK) {
                    removeChoiceByQuizzIds.add(quizzUpdate.getId());
                } else if (quizzUpdate.getType() == QuizzType.MULTIPLECHOICE && question.getQType() == QuizzType.MULTIPLECHOICE) {
                    Choice newChoiceA = quizzUpdate.getChoices().stream().filter(e -> e.getId() == question.getQA().getId()).findFirst().orElseThrow(() -> new Exception("can not find choice"));
                    newChoiceA.setContent(question.getQA().getContent());


                    Choice newChoiceB = quizzUpdate.getChoices().stream().filter(e -> e.getId() == question.getQB().getId()).findFirst().orElseThrow(() -> new Exception("can not find choice"));
                    newChoiceB.setContent(question.getQB().getContent());


                    Choice newChoiceC = quizzUpdate.getChoices().stream().filter(e -> e.getId() == question.getQC().getId()).findFirst().orElseThrow(() -> new Exception("can not find choice"));
                    newChoiceC.setContent(question.getQC().getContent());


                    Choice newChoiceD = quizzUpdate.getChoices().stream().filter(e -> e.getId() == question.getQD().getId()).findFirst().orElseThrow(() -> new Exception("can not find choice"));
                    newChoiceD.setContent(question.getQD().getContent());


                    batchUpdateChoice.add(newChoiceA);
                    batchUpdateChoice.add(newChoiceB);
                    batchUpdateChoice.add(newChoiceC);
                    batchUpdateChoice.add(newChoiceD);
                } else if (quizzUpdate.getType() == QuizzType.FILLINTHEBLANK && question.getQType() == QuizzType.MULTIPLECHOICE) {
                    Choice newChoiceA = new Choice();
                    newChoiceA.setContent(question.getQA().getContent());
                    newChoiceA.setQuizzId(quizzUpdate.getId());


                    Choice newChoiceB = new Choice();
                    newChoiceB.setContent(question.getQA().getContent());
                    newChoiceB.setQuizzId(quizzUpdate.getId());

                    Choice newChoiceC = new Choice();
                    newChoiceC.setContent(question.getQA().getContent());
                    newChoiceC.setQuizzId(quizzUpdate.getId());

                    Choice newChoiceD = new Choice();
                    newChoiceD.setContent(question.getQA().getContent());
                    newChoiceD.setQuizzId(quizzUpdate.getId());

                    batchUpdateChoice.add(newChoiceA);
                    batchUpdateChoice.add(newChoiceB);
                    batchUpdateChoice.add(newChoiceC);
                    batchUpdateChoice.add(newChoiceD);
                }

                quizzUpdate.setType(question.getQType());
                quizzUpdate.setQuestion(question.getQQuestion());
                quizzUpdate.setAnswer(question.getQAnswer());
                batchUpdateQuestion.add(quizzUpdate);
            }


        }
        removeChoiceByQuizzIds.addAll(removeQuestionIds);
        choiceRepository.deleteAllByQuizzIds(removeChoiceByQuizzIds);
        quizzRepository.deleteAllById(removeQuestionIds);
        quizzRepository.saveAll(batchUpdateQuestion);
        batchUpdateChoice = batchUpdateChoice.stream().peek(e -> {
            if (e.getQuizzId() == 0)
                e.setQuizzId(e.getQuizz().getId());
        }).collect(Collectors.toList());
        choiceRepository.saveAll(batchUpdateChoice);
        return new ArrayList<>();
    }

    @Override
    public Lesson updateLesson(Lesson request) throws Exception {
        Lesson updateLesson = lessonRepository.findById(request.getId()).orElseThrow(() -> new Exception("can not find section"));
        long deletedMediaId = 0;

        if(request.getType() == LessonType.VIDEO && updateLesson.getType() == LessonType.VIDEO)
        {
            if(request.getMediaId() != null && request.getMediaId() != 0){
                deleteOldVideoAndThumbnail(updateLesson.getMedia());
                long lessonMediaId = updateLesson.getMediaId();
                updateLesson.setMediaId(request.getMediaId());
                deletedMediaId = lessonMediaId;

            }
        }else  if(request.getType() == LessonType.TEXT && updateLesson.getType() == LessonType.VIDEO){
            deleteOldVideoAndThumbnail(updateLesson.getMedia());
            long lessonMediaId = updateLesson.getMediaId();
            updateLesson.setMediaId(null);
            deletedMediaId = lessonMediaId;

        }else if(request.getType() == LessonType.VIDEO && updateLesson.getType() == LessonType.TEXT){
            updateLesson.setMediaId(request.getMediaId());

        }

        updateLesson.setContent(request.getContent());
        updateLesson.setTitle(request.getTitle());
        updateLesson.setPassThreshold(request.getPassThreshold());
        updateLesson.setType(request.getType());

        lessonRepository.save(updateLesson);
        if(deletedMediaId!=0) {
            imageRepository.deleteById(deletedMediaId);
        }
        return null;
    }
    private void deleteOldVideoAndThumbnail(UploadFile uploadFile) {
        // Xóa tệp video cũ
        File oldVideoFile = new File(uploadFile.getOriginUrl());
        if (oldVideoFile.exists()) {
            oldVideoFile.delete();
        }

        // Xóa hình ảnh minh họa cũ
        File oldThumbnailFile = new File(uploadFile.getThumbUrl());
        if (oldThumbnailFile.exists()) {
            oldThumbnailFile.delete();
        }
    }

}
