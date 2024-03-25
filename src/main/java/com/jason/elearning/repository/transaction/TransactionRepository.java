package com.jason.elearning.repository.transaction;

import com.jason.elearning.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> ,TransactionRepositoryCustom{
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE FUNCTION('YEAR', t.createdAt) = :year")
    Long getTotalProfitByYear(int year);



}
