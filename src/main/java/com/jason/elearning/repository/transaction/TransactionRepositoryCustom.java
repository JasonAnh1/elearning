package com.jason.elearning.repository.transaction;

import java.util.List;
import java.util.Map;

public interface TransactionRepositoryCustom {

    List<Map<String, Object>> getTotalAmountPerMonth(int year);

    List<Map<String, Object>> getTotalAmountPerDay(int year,int month);

    Long calculateNetProfitByYear(int year);
}
