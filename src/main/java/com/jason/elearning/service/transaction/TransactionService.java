package com.jason.elearning.service.transaction;

import com.jason.elearning.entity.User;
import com.jason.elearning.entity.request.VerifyRequest;

import java.util.List;
import java.util.Map;

public interface TransactionService {
     List<Map<String, Object>> getTotalAmountPerMonth(int year);
     List<Map<String, Object>> getTotalAmountPerDay(int year,int month) throws Exception;
     User saveVerify(VerifyRequest request) throws Exception;
     Long revenueGrossProfit(int year) throws Exception;
     Long revenueNetProfit(int year) throws Exception;
     Long lectureRevenueNetProfit(int year) throws Exception;
}
