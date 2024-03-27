package com.jason.elearning.service.enroll;

import com.jason.elearning.configuration.Translator;
import com.jason.elearning.entity.*;
import com.jason.elearning.entity.constants.EnrollStatus;
import com.jason.elearning.entity.constants.TransactionStatus;
import com.jason.elearning.entity.constants.TransactionType;
import com.jason.elearning.entity.request.ItemOrder;
import com.jason.elearning.entity.request.PlaceOrderRequest;
import com.jason.elearning.repository.course.LessonRepository;
import com.jason.elearning.repository.enroll.LessonProgressRepository;
import com.jason.elearning.repository.transaction.TransactionRepository;
import com.jason.elearning.repository.user.EnrollRepository;
import com.jason.elearning.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollServiceImpl extends BaseService implements EnrollService{

    @Autowired
    private EnrollRepository enrollRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private LessonProgressRepository lessonProgressRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Override
    public List<Enroll> placeOrder(PlaceOrderRequest request) throws Exception {
        Transaction trans = new Transaction();
        User user = getUser();
        if (user ==null) {
            throw new Exception(Translator.toLocale("access_denied"));
        }

        trans.setAmount(request.getTotal());
        trans.setTransCode(request.getTransCode());
        trans.setType(TransactionType.CONTENT_PAYMENT);
        trans.setStatus(TransactionStatus.SUCCESS);
        trans.setSender_id(user.getId());
        trans.setReceiver("SYSTEM");


        List<ItemOrder> items = request.getItems();

        for (ItemOrder item : items) {
            if (item.getType().equals("COURSE")) {
                // Tạo một bản ghi enroll mới biểu thị một khóa học được đặt hàng
                Enroll enroll = new Enroll();
                enroll.setUserId(user.getId()); // Liên kết với người dùng đặt hàng
                enroll.setCourseId(item.getId()); // Thiết lập ID của khóa học
                enroll.setStatus(EnrollStatus.ENROLLED);
                enroll.setProgress(0);
                enrollRepository.save(enroll); // Lưu bản ghi enroll vào cơ sở dữ liệu

            }
            // Có thể xử lý các loại item khác ở đây nếu cần
        }

        transactionRepository.save(trans);
        return enrollRepository.findByUserId(user.getId());
    }

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
