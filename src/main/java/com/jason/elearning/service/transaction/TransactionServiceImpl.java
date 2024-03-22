package com.jason.elearning.service.transaction;

import com.jason.elearning.configuration.Translator;
import com.jason.elearning.entity.Transaction;
import com.jason.elearning.entity.User;
import com.jason.elearning.entity.constants.TransactionStatus;
import com.jason.elearning.entity.constants.TransactionType;
import com.jason.elearning.entity.constants.UserActive;
import com.jason.elearning.entity.request.VerifyRequest;
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

@Service
public class TransactionServiceImpl extends BaseService implements TransactionService{
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Override
    public List<Map<String, Object>> getTotalAmountPerMonth(int year) {
        return transactionRepository.getTotalAmountPerMonth(year);
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
}
