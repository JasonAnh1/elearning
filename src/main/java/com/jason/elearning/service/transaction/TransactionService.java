package com.jason.elearning.service.transaction;

import com.jason.elearning.entity.User;
import com.jason.elearning.entity.request.VerifyRequest;

import java.util.List;
import java.util.Map;

public interface TransactionService {
     List<Map<String, Object>> getTotalAmountPerMonth(int year);
     User saveVerify(VerifyRequest request) throws Exception;
}
