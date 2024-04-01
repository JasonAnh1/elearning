package com.jason.elearning.repository.transaction;

import com.jason.elearning.entity.QTransaction;
import com.jason.elearning.entity.constants.TransactionStatus;
import com.jason.elearning.repository.BaseRepository;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.util.List;
import java.util.Map;

public class TransactionRepositoryImpl extends BaseRepository implements TransactionRepositoryCustom{
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

    @Override
    public Long calculateLectureNetProfitByYear(int year, long lectureId) {

        QTransaction qTransaction =  QTransaction.transaction;
        BooleanBuilder builder = new BooleanBuilder();

        // Xác định các điều kiện cho query DSL
        builder.and(qTransaction.receiver_id.eq(lectureId)
                .and(qTransaction.status.eq(TransactionStatus.SUCCESS))
                .and(qTransaction.createdAt.year().eq(year))); // Giả sử bạn có một method tên createdYear để trích xuất năm từ timestamp

        // Thực hiện truy vấn và tính tổng số lượng
        Long totalAmount = query().from(qTransaction)
                .where(builder)
                .select(qTransaction.amount.sum())
                .fetchOne();

        return totalAmount != null ? totalAmount : 0L;
    }
}
