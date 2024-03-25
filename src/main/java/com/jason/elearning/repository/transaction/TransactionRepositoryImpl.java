package com.jason.elearning.repository.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.util.List;
import java.util.Map;

public class TransactionRepositoryImpl implements TransactionRepositoryCustom{
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;
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

    @Override
    public Long calculateNetProfitByYear(int year) {
        StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("CalculateNetProfitByYear");

        // Đặt tham số đầu vào cho stored procedure
        storedProcedure.registerStoredProcedureParameter("input_year", Integer.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("net_profit", Long.class, ParameterMode.OUT);

        // Đặt giá trị cho tham số đầu vào
        storedProcedure.setParameter("input_year", year);

        // Thực thi stored procedure
        storedProcedure.execute();

        // Lấy kết quả từ tham số đầu ra
        Long netProfit = (Long) storedProcedure.getOutputParameterValue("net_profit");

        return netProfit;
    }
}
