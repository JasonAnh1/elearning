package com.jason.elearning.repository.course;

import com.jason.elearning.entity.Choice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChoiceRepository extends JpaRepository<Choice,Long> {

    @Modifying
    @Query("DELETE FROM Choice e WHERE e.quizzId IN :quizzIds")
    void deleteAllByQuizzIds(@Param("quizzIds") List<Long> quizzIds);
    List<Choice> findAllByQuizzId(Long quizzId);
}
