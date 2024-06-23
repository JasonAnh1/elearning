package com.jason.elearning.repository.transaction;

import com.jason.elearning.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> ,TransactionRepositoryCustom{
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE FUNCTION('YEAR', t.createdAt) = :year")
    Long getTotalProfitByYear(int year);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.receiver_id = :receiverId AND FUNCTION('YEAR', t.createdAt) = :year AND FUNCTION('MONTH', t.createdAt) = :month")
    Optional<Long> findTotalAmountByReceiverIdAndYearAndMonth(@Param("receiverId") Long receiverId, @Param("year") int year, @Param("month") int month);

}
