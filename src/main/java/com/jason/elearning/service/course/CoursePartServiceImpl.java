package com.jason.elearning.service.course;

import com.jason.elearning.configuration.Translator;
import com.jason.elearning.entity.*;
import com.jason.elearning.entity.constants.LessonType;
import com.jason.elearning.entity.constants.QuizzType;
import com.jason.elearning.entity.request.AnswerSheetRequest;
import com.jason.elearning.entity.request.QuizzRequest;
import com.jason.elearning.entity.request.QuizzesRequest;
import com.jason.elearning.entity.request.UpdateQuestionRequest;
import com.jason.elearning.repository.ImageRepository;
import com.jason.elearning.repository.comment.LessonCommentRepository;
import com.jason.elearning.repository.course.*;
import com.jason.elearning.repository.enroll.LessonProgressRepository;
import com.jason.elearning.repository.user.EnrollRepository;
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
    @Autowired
    private LessonProgressRepository lessonProgressRepository;
    @Autowired
    private LessonCommentRepository lessonCommentRepository;
    @Autowired
    private EnrollRepository enrollRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Override
    public CoursePart addSection(CoursePart coursePart) {

        List<CoursePart> lstCoursePart = coursePartRepository.findAllByCourseIdOrderByPartNumber(coursePart.getCourseId());
        if (lstCoursePart.size() == 0) {
            coursePart.setPartNumber(1);
        } else {
            int maxPartNumber = lstCoursePart.get(lstCoursePart.size() - 1).getPartNumber();
            if (coursePart.getPartNumber() - maxPartNumber > 1) {
                coursePart.setPartNumber(maxPartNumber + 1);
            } else {
                for (CoursePart cp : lstCoursePart) {
                    if (cp.getPartNumber() >= coursePart.getPartNumber()) {
                        cp.setPartNumber(cp.getPartNumber() + 1);
                    }
                }
                coursePartRepository.saveAll(lstCoursePart);
            }

        }

        return coursePartRepository.save(coursePart);
    }

    @Override
    public CoursePart updateSection(CoursePart request) throws Exception {
        CoursePart cp = coursePartRepository.findById(request.getId()).orElseThrow(() -> new Exception("can not find section"));
        cp.setTitle(request.getTitle());

        int requestPosition = request.getPartNumber();
        int currentPosition = cp.getPartNumber();
        if (requestPosition != currentPosition) {

            List<CoursePart> lstCoursePart = coursePartRepository.findAllByCourseIdOrderByPartNumber(request.getCourseId());
            int lastPosition = lstCoursePart.get(lstCoursePart.size() - 1).getPartNumber();

            if (requestPosition < currentPosition) {
                for (CoursePart coursePart : lstCoursePart) {
                    if (coursePart.getPartNumber() >= requestPosition && coursePart.getPartNumber() < currentPosition) {
                        coursePart.setPartNumber(coursePart.getPartNumber() + 1);
                    }
                }
            } else {
                for (CoursePart coursePart : lstCoursePart) {
                    if (coursePart.getPartNumber() > currentPosition && coursePart.getPartNumber() <= requestPosition) {
                        coursePart.setPartNumber(coursePart.getPartNumber() - 1);
                    }
                }
            }

            if (requestPosition > lastPosition) {
                cp.setPartNumber(lastPosition);
            } else {
                cp.setPartNumber(requestPosition);
            }

        }

        return coursePartRepository.save(cp);
    }

    @Override
    public List<CoursePart> listCourseSession(long courseId) {


        return coursePartRepository.getAllCourseSession(courseId);
    }

    @Override
    public Lesson addLesson(Lesson lesson) throws Exception {
        // lấy vị trí lesson mà người dùng muốn đặt lesson vào
        int requestPosition = lesson.getPosition();
        // lấy tất cả lesson trong course part đó
        List<Lesson> lstLesson = lessonRepository.findAllByCoursePartIdOrderByPositionAsc(lesson.getCoursePartId());
        // kiểm tra size
        if (lstLesson.size() == 0) {
            // set lesson mà người dùng muốn đặt về 1 nếu course part rỗng
            lesson.setPosition(1);
        } else {
            // lấy vị trí cuối cùng của lesson trong course part
            int lastPosition = lstLesson.get(lstLesson.size() - 1).getPosition();
            // nếu vị trí mà người dùng muốn đặt lớn hơn 1 bước nhảy thì đặt về 1 bước nhảy sau vị trí cuối
            if (requestPosition - lastPosition > 1) {
                lesson.setPosition(lastPosition + 1);
            } else {
                // ngược lại dịch tất cả các vị trí sao cho vị trí người dùng muốn đặt hợp lệ
                for (Lesson ls : lstLesson) {
                    if (ls.getPosition() >= requestPosition) {
                        ls.setPosition(ls.getPosition() + 1);
                    }
                }
                lessonRepository.saveAll(lstLesson);
            }

        }

        Lesson savedLesson = lessonRepository.save(lesson);
        CoursePart coursePart = coursePartRepository.findById(lesson.getCoursePartId()).orElseThrow(() -> new Exception("can not find section"));
        long courseId = coursePart.getCourseId();
        List<Enroll> lstEnrolled = enrollRepository.findAllByCourseId(courseId);
        List<Long> lstUserIdEnrolled = lstEnrolled.stream()
                .map(Enroll::getUserId)
                .collect(Collectors.toList());

        lessonProgressRepository.saveAll(addLessonProgressOfNewLessonForUserEnrolled(lstUserIdEnrolled
                , savedLesson.getId(), isFirstLessonOfACourse(savedLesson, courseId)));

        return savedLesson;
    }

    public boolean isFirstLessonOfACourse(Lesson lesson, long courseId) throws Exception {

        List<CoursePart> lstCoursePart = coursePartRepository.findAllByCourseIdOrderByPartNumber(courseId);
        if(lesson.getId() == lstCoursePart.get(0).getLessons().get(0).getId() == true){
            lesson.setFree(true);
            lessonRepository.save(lesson);
        }
        return lesson.getId() == lstCoursePart.get(0).getLessons().get(0).getId();
    }

    ;

    public List<LessonProgress> addLessonProgressOfNewLessonForUserEnrolled(List<Long> lstEnrolledUserIds, Long lessonId, boolean isFirstLessonOfCourse) {
        List<LessonProgress> addLessonProgressList = new ArrayList<>();
        for (Long userId : lstEnrolledUserIds) {
            LessonProgress lessonProgress = new LessonProgress();
            lessonProgress.setProgress(0.0);
            lessonProgress.setUserId(userId);
            lessonProgress.setLessonId(lessonId);
            lessonProgress.setLocked(!isFirstLessonOfCourse);
            addLessonProgressList.add(lessonProgress);

        }

        return addLessonProgressList;
    }

    ;

    @Override
    public Lesson getLessonById(long id) throws Exception {

        Lesson lesson = lessonRepository.findById(id).orElseThrow(() -> new Exception("can not find lesson"));

        if (quizzRepository.existsByLessonId(lesson.getId())) {
            lesson.setHaveTest(true);
        }
        return lesson;
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

        if (request.getType() == LessonType.VIDEO && updateLesson.getType() == LessonType.VIDEO) {
            if (request.getMediaId() != null && request.getMediaId() != 0) {
                deleteOldVideoAndThumbnail(updateLesson.getMedia());
                long lessonMediaId = updateLesson.getMediaId();
                updateLesson.setMediaId(request.getMediaId());
                deletedMediaId = lessonMediaId;

            }
        } else if (request.getType() == LessonType.TEXT && updateLesson.getType() == LessonType.VIDEO) {
            deleteOldVideoAndThumbnail(updateLesson.getMedia());
            long lessonMediaId = updateLesson.getMediaId();
            updateLesson.setMediaId(null);
            deletedMediaId = lessonMediaId;

        } else if (request.getType() == LessonType.VIDEO && updateLesson.getType() == LessonType.TEXT) {
            updateLesson.setMediaId(request.getMediaId());

        }

        updateLesson.setContent(request.getContent());
        updateLesson.setTitle(request.getTitle());
        updateLesson.setPassThreshold(request.getPassThreshold());
        updateLesson.setType(request.getType());
        updateLesson.setFree(request.isFree());


        int requestPosition = request.getPosition();
        int currentPosition = updateLesson.getPosition();
        if (requestPosition != currentPosition) {
            // lấy tất cả lesson trong course part đó
            List<Lesson> lstLesson = lessonRepository.findAllByCoursePartIdOrderByPositionAsc(updateLesson.getCoursePartId());
            int lastPosition = lstLesson.get(lstLesson.size() - 1).getPosition();

            if (requestPosition < currentPosition) {
                for (Lesson ls : lstLesson) {
                    if (ls.getPosition() >= requestPosition && ls.getPosition() < currentPosition) {
                        ls.setPosition(ls.getPosition() + 1);
                    }
                }
            } else {
                for (Lesson ls : lstLesson) {
                    if (ls.getPosition() > currentPosition && ls.getPosition() <= requestPosition) {
                        ls.setPosition(ls.getPosition() - 1);
                    }
                }
            }

            if (requestPosition > lastPosition) {
                updateLesson.setPosition(lastPosition);
            } else {
                updateLesson.setPosition(requestPosition);
            }

        }

        lessonRepository.save(updateLesson);
        if (deletedMediaId != 0) {
            imageRepository.deleteById(deletedMediaId);
        }

        return null;
    }

    @Override
    public List<CoursePart> listLearningLesson(long id) throws Exception {

        User user = getUser();
        if (user == null) {
            throw new Exception(Translator.toLocale("access_denied"));
        }


        List<CoursePart> lstSections = coursePartRepository.getAllCourseSession(id);
        List<Long> lessonIds = lstSections.stream()
                .map(e -> e.getLessons().stream().map(Lesson::getId).collect(Collectors.toList()))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        List<LessonProgress> lessonProgresses = lessonRepository.listLearningLessonProgress(user.getId(), lessonIds);

        lstSections.forEach(e -> e.getLessons().forEach(i -> {
            try {
                LessonProgress lessonProgress = lessonProgresses.stream()
                        .filter(j -> j.getLessonId() == i.getId())
                        .findFirst()
                        .orElseThrow(() -> new Exception("can not find lesson"));
                i.setLock(lessonProgress.getLocked());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }));

        return lstSections;
    }

    @Override
    public String checkAnswer(AnswerSheetRequest request) throws Exception {
        User user = getUser();
        if (user == null) {
            throw new Exception(Translator.toLocale("access_denied"));
        }

        List<Quizz> lstQuizzes = quizzRepository.findAllByLessonId(request.getLessonId());
        // update 100 progress
        LessonProgress lessonProgressUpdate = lessonProgressRepository.findFirstByUserIdAndLessonId(user.getId(), request.getLessonId());
        lessonProgressUpdate.setProgress(100.0);
        lessonProgressRepository.save(lessonProgressUpdate);
        Lesson lesson = lessonRepository.findById(request.getLessonId())
                .orElseThrow(() -> new Exception("can not find lesson"));
        int passThreshold = lesson.getPassThreshold();
        if (lstQuizzes == null) {
            throw (new Exception("can not find lesson"));
        }
        int correctCount = (int) request.answers.stream().filter(
                e -> lstQuizzes.stream().anyMatch(
                        i -> i.getId() == e.getId() && i.getAnswer().trim().equals(e.getAnswer().trim())
                )
        )
                .distinct().count();
        if (correctCount >= passThreshold) {
            Lesson lessonUnlock = lessonRepository
                    .findFirstByPart_IdAndPosition(lesson.getCoursePartId(), lesson.getPosition() + 1)
                    .orElseGet(() -> {
                        CoursePart currentPart = lesson.getPart();
                        CoursePart nextCoursePart = coursePartRepository
                                .findFirstByCourseIdAndPartNumber(currentPart.getCourseId(), currentPart.getPartNumber() + 1)
                                .orElse(null);
                        return nextCoursePart == null
                                ? null
                                : nextCoursePart.getLessons().stream().findFirst().orElse(null);
                    });
            if (lessonUnlock != null) {
                LessonProgress lessonProgress = lessonProgressRepository
                        .findFirstByUserIdAndLessonId(user.getId(), lessonUnlock.getId());
                lessonProgress.setLocked(false);
                lessonProgressRepository.save(lessonProgress);
            }

            return "PASS";
        }

        return "FAILED";
    }

    @Override
    public String unlockLesson(long lessonId) throws Exception {
        User user = getUser();
        if (user == null) {
            throw new Exception(Translator.toLocale("access_denied"));
        }
        // update 100 progress
        LessonProgress lessonProgressUpdate = lessonProgressRepository.findFirstByUserIdAndLessonId(user.getId(), lessonId);
        lessonProgressUpdate.setProgress(100.0);
        lessonProgressRepository.save(lessonProgressUpdate);

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new Exception("can not find lesson"));
        // find next lesson to unlock
        Lesson lessonUnlock = lessonRepository
                .findFirstByPart_IdAndPosition(lesson.getCoursePartId(), lesson.getPosition() + 1)
                .orElseGet(() -> {
                    CoursePart currentPart = lesson.getPart();
                    CoursePart nextCoursePart = coursePartRepository
                            .findFirstByCourseIdAndPartNumber(currentPart.getCourseId(), currentPart.getPartNumber() + 1)
                            .orElse(null);
                    return nextCoursePart == null
                            ? null
                            : nextCoursePart.getLessons().stream().findFirst().orElse(null);
                });
        if (lessonUnlock != null) {
            LessonProgress lessonProgress = lessonProgressRepository
                    .findFirstByUserIdAndLessonId(user.getId(), lessonUnlock.getId());
            lessonProgress.setLocked(false);
            lessonProgressRepository.save(lessonProgress);
            return "SUCCESS";
        }

        return "FAILED";
    }

    @Override
    public void deleteLesson(long lessonId) throws Exception {
        User user = getUser();
        if (user == null) {
            throw new Exception(Translator.toLocale("access_denied"));
        }
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new Exception("can not find lesson"));

        long coursePartId = lesson.getCoursePartId();
        int deletedPosition = lesson.getPosition();

        deleteAllRelateOfLesson(lesson);
        lessonRepository.deleteById(lessonId);

        lessonRepository.flush();
        List<Lesson> listRepositionLesson = lessonRepository.findAllByCoursePartIdOrderByPositionAsc(coursePartId);
        if (listRepositionLesson.size() >= 1) {
            List<Lesson> listSaveLesson = new ArrayList<>();
            for (Lesson repositionLesson : listRepositionLesson) {
                if (repositionLesson.getPosition() > deletedPosition) {
                    repositionLesson.setPosition(repositionLesson.getPosition() - 1);
                    listSaveLesson.add(repositionLesson);
                }
            }
            lessonRepository.saveAll(listSaveLesson);
        }
    }

    private void deleteAllRelateOfLesson(Lesson lesson) {

        List<Quizz> deleteListQuizzes = quizzRepository.findAllByLessonId(lesson.getId());
        List<LessonProgress> deleteListLessonProgress = lessonProgressRepository.findAllByLessonId(lesson.getId());
        List<LessonComment> deleteLessonComment = lessonCommentRepository.findAllByLessonIdAndParentCommentIdIsNullOrderByUpdatedAtDesc(lesson.getId());
        if (deleteLessonComment.size() > 0) {
            for (LessonComment lsComment : deleteLessonComment) {
                List<LessonComment> ls = lessonCommentRepository.findAllByParentCommentId(lsComment.getId());
                lessonCommentRepository.deleteAll(ls);
            }
            lessonCommentRepository.deleteAll(deleteLessonComment);

        }
        if (deleteListLessonProgress.size() > 0) {
            lessonProgressRepository.deleteAll(deleteListLessonProgress);
        }
        if (deleteListQuizzes.size() > 0) {
            quizzRepository.deleteAll(deleteListQuizzes);
        }

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
