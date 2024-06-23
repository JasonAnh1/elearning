package com.jason.elearning.service.transaction;

import com.jason.elearning.configuration.Translator;
import com.jason.elearning.entity.Course;
import com.jason.elearning.entity.Transaction;
import com.jason.elearning.entity.User;
import com.jason.elearning.entity.constants.*;
import com.jason.elearning.entity.request.PromoteRequest;
import com.jason.elearning.entity.request.VerifyRequest;
import com.jason.elearning.repository.course.CourseRepository;
import com.jason.elearning.repository.transaction.TransactionRepository;
import com.jason.elearning.repository.user.UserRepository;
import com.jason.elearning.security.CustomUserDetailsService;
import com.jason.elearning.security.JwtTokenProvider;
import com.jason.elearning.security.UserPrincipal;
import com.jason.elearning.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TransactionServiceImpl extends BaseService implements TransactionService{
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Override
    public List<Map<String, Object>> getTotalAmountPerMonth(int year) {
        return transactionRepository.getTotalAmountPerMonth(year);
    }

    @Override
    public List<Map<String, Object>> getTotalAmountPerDay(int year, int month) throws Exception {
        User user = getUser();
        if (user ==null) {
            throw new Exception(Translator.toLocale("access_denied"));
        }
        if (user.getRoles().get(0).getName() != RoleName.ROLE_ADMIN) {
            throw new Exception(Translator.toLocale("access_denied"));
        }

        return transactionRepository.getTotalAmountPerDay(year,month);
    }

    @Override
    public User saveVerify(VerifyRequest request) throws Exception {
        User user = getUser();
        if (user ==null) {
            throw new Exception(Translator.toLocale("access_denied"));
        }
        user.setActive(UserActive.VERIFY);
        Transaction transaction = new Transaction();
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setAmount(request.amount);
        transaction.setSender_id(user.getId());
        transaction.setTransCode(request.getTransCode());
        transaction.setType(TransactionType.VERIFY_FEE);
        transactionRepository.save(transaction);
        User result = userRepository.save(user);
//        sendNotification(result);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(result.getPhone());
        String jwt = tokenProvider.generateTokenByUser((UserPrincipal) userDetails);
        result.setAccessToken(jwt);
        return result;
    }

    @Override
    public Long revenueGrossProfit(int year) throws Exception {
        User user = getUser();
        if (user ==null) {
            throw new Exception(Translator.toLocale("access_denied"));
        }
        if (user.getRoles().get(0).getName() != RoleName.ROLE_ADMIN) {
            throw new Exception(Translator.toLocale("access_denied"));
        }

        return transactionRepository.getTotalProfitByYear(year);
    }

    @Override
    public Long revenueNetProfit(int year) throws Exception {
        User user = getUser();
        if (user ==null) {
            throw new Exception(Translator.toLocale("access_denied"));
        }
        if (user.getRoles().get(0).getName() != RoleName.ROLE_ADMIN) {
            throw new Exception(Translator.toLocale("access_denied"));
        }

        return transactionRepository.calculateNetProfitByYear(year);
    }

    @Override
    public Long lectureRevenueNetProfit(int year) throws Exception {
        User user = getUser();
        if (user ==null) {
            throw new Exception(Translator.toLocale("access_denied"));
        }

        return transactionRepository.calculateLectureNetProfitByYear(year, user.getId());
    }

    @Override
    public Course savePromote(PromoteRequest request) throws Exception {
        User user = getUser();
        if (user ==null) {
            throw new Exception(Translator.toLocale("access_denied"));
        }
        Transaction transaction = new Transaction();
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setAmount(request.amount);
        transaction.setSender_id(user.getId());
        transaction.setTransCode(request.getTransCode());
        transaction.setType(TransactionType.PROMOTION_FEE);
        transactionRepository.save(transaction);

        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(() -> new Exception("can not find course"));
        course.setAdvertise(CourseAdvertise.PROMOTE);
        return courseRepository.save(course);
    }
    @Override
    public Optional<Long> getTotalAmountByReceiverIdAndYearAndMonth(Long receiverId, int year, int month) {
        return transactionRepository.findTotalAmountByReceiverIdAndYearAndMonth(receiverId, year, month);
    }
}
