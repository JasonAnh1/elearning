package com.jason.elearning.repository.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

public class TransactionRepositoryImpl implements TransactionRepositoryCustom{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> getTotalAmountPerMonth(int year) {
        String sql = "CALL GetTotalAmountPerYear(?)";
        return jdbcTemplate.queryForList(sql,year);
    }

    @Override
    public List<Map<String, Object>> getTotalAmountPerDay(int year, int month) {
        String sql = "CALL GetTotalAmountPerMonth(?,?)";
        return jdbcTemplate.queryForList(sql,year,month);
    }
}
