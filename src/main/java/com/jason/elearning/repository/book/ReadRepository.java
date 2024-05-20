package com.jason.elearning.repository.book;

import com.jason.elearning.entity.Read;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReadRepository extends JpaRepository<Read,Long> {
    Read findFirstByUserIdAndBookId(Long userId,Long bookId);
    List<Read> findAllByUserId(Long userId);
}
