package com.jason.elearning.service.enroll;

import com.jason.elearning.configuration.Translator;
import com.jason.elearning.entity.Enroll;
import com.jason.elearning.entity.Transaction;
import com.jason.elearning.entity.User;
import com.jason.elearning.entity.constants.EnrollStatus;
import com.jason.elearning.entity.constants.TransactionStatus;
import com.jason.elearning.entity.constants.TransactionType;
import com.jason.elearning.entity.request.ItemOrder;
import com.jason.elearning.entity.request.PlaceOrderRequest;
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
        trans.setRemiters(user.getName());

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
}
