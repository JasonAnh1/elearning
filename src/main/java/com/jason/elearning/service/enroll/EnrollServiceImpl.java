package com.jason.elearning.service.enroll;

import com.jason.elearning.configuration.Translator;
import com.jason.elearning.entity.*;
import com.jason.elearning.entity.constants.EnrollStatus;
import com.jason.elearning.entity.constants.TransactionStatus;
import com.jason.elearning.entity.constants.TransactionType;
import com.jason.elearning.entity.request.ItemOrder;
import com.jason.elearning.entity.request.PlaceOrderRequest;
import com.jason.elearning.repository.course.CoursePartRepository;
import com.jason.elearning.repository.course.CourseRepository;
import com.jason.elearning.repository.course.LessonRepository;
import com.jason.elearning.repository.enroll.LessonProgressRepository;
import com.jason.elearning.repository.transaction.TransactionRepository;
import com.jason.elearning.repository.user.EnrollRepository;
import com.jason.elearning.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EnrollServiceImpl extends BaseService implements EnrollService{

    @Autowired
    private EnrollRepository enrollRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private LessonProgressRepository lessonProgressRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private CoursePartRepository coursePartRepository;
    @Autowired
    private CourseRepository courseRepository;


    @Override
    public List<Enroll> placeOrder(PlaceOrderRequest request) throws Exception {

        User user = getUser();
        if (user ==null) {
            throw new Exception(Translator.toLocale("access_denied"));
        }
        List<Transaction> lstTransactions = new ArrayList<>();

        List<ItemOrder> items = request.getItems();
        List<List<LessonProgress>> lessonProgresses = new ArrayList<>();
        for (ItemOrder item : items) {
            if (item.getType().equals("COURSE")) {

                Long courseId = item.getId();
                // lấy khóa học
                Course course = courseRepository.findById(courseId)
                        .orElseThrow(() -> new RuntimeException("Cannot find course."));
                // lấy id của lecture
                Long lectureId = course.getAuthor().getId();

                // tạo 1 transaction với từng khóa học để có thể thống kê được doanh thu cho lecture sau này
                Transaction trans = new Transaction();
                trans.setAmount(course.getPriceSale());
                trans.setTransCode(request.getTransCode());
                trans.setType(TransactionType.CONTENT_PAYMENT);
                trans.setStatus(TransactionStatus.SUCCESS);
                trans.setSender_id(user.getId());
                trans.setReceiver_id(lectureId);
                // add vào lst transaction đã khởi tạo
                lstTransactions.add(trans);

                // Tạo một bản ghi enroll mới biểu thị một khóa học được đặt hàng

                Enroll enroll = new Enroll();
                enroll.setUserId(user.getId()); // Liên kết với người dùng đặt hàng
                enroll.setCourseId(item.getId()); // Thiết lập ID của khóa học
                enroll.setStatus(EnrollStatus.ENROLLED);
                enroll.setProgress(0);
                enrollRepository.save(enroll); // Lưu bản ghi enroll vào cơ sở dữ liệu


                List<CoursePart> coursePartList = coursePartRepository.findAllByCourseIdOrderByPartNumber(courseId);
                Long firstLessonId = 0L;
                try {
                     firstLessonId = coursePartList.get(0).getLessons().get(0).getId();

                    List<Long> lessonIds = listLessonId(item.getId());
                    List<LessonProgress> list = lessonIds.stream()
                            .map(lessonId -> new LessonProgress().builder()
                                    .lessonId(lessonId)
                                    .locked(true)
                                    .userId(user.getId())
                                    .build()).collect(Collectors.toList());
                    Long finalFirstLessonId = firstLessonId;
                    list.forEach(lessonProgress -> {
                        if(lessonProgress.getLessonId() == finalFirstLessonId) {
                            lessonProgress.setLocked(false);
                        }
                    });

                    lessonProgresses.add(list);

                } catch (Exception ignored) {
                }

            }
        }

        List<LessonProgress> res = lessonProgresses.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        lessonProgressRepository.saveAll(res);
        transactionRepository.saveAll(lstTransactions);
        return enrollRepository.findByUserId(user.getId());
    }

    public List<Long> listLessonId(Long courseId){
      List<Long> lessonIds  = new ArrayList<>();
      List<CoursePart> lstCoursePart = coursePartRepository.findAllByCourseId(courseId);
      if(lstCoursePart.size() > 0 ){
          lessonIds =  lstCoursePart
                  .stream()
                  .map(coursePart -> coursePart.getLessons())
                  .flatMap(Collection::stream)
                  .map(lesson -> lesson.getId()).collect(Collectors.toList());
      }


//      for(long id: coursePartId){
//          Lesson lesson = le
//      }

      return lessonIds;

    };
    @Override
    public void updateVideoProgress(double progress,long lessonId) throws Exception {
        User user = getUser();
        if (user ==null) {
            throw new Exception(Translator.toLocale("access_denied"));
        }

        LessonProgress lessonProgress = lessonProgressRepository.findFirstByUserIdAndLessonId(user.getId(),lessonId);

        if(lessonProgress == null){
            LessonProgress lessonProgress1 = new LessonProgress();
            lessonProgress1.setVideoProgress(progress);
            lessonProgress1.setUserId(user.getId());
            lessonProgress1.setLessonId(lessonId);
            lessonProgress1.setProgress(0.0);
            lessonProgressRepository.save(lessonProgress1);
        }else {
            Lesson lesson = lessonProgress.getLesson();
            if (lesson.getPassThreshold() == 0 && progress == 100) {
                lessonProgress.setProgress(100.0);
            }
            lessonProgress.setVideoProgress(progress);
            lessonProgressRepository.save(lessonProgress);
        }

    }

    @Override
    public double loadVideoProgress(long lessonId) throws Exception {

        User user = getUser();
        if (user ==null) {
            throw new Exception(Translator.toLocale("access_denied"));
        }
        LessonProgress lessonProgress = lessonProgressRepository.findFirstByUserIdAndLessonId(user.getId(),lessonId);
        if(lessonProgress == null){
            return 0;
        }else {
            return lessonProgress.getVideoProgress();
        }

    }
}
