package com.jason.elearning.repository.course;

import com.jason.elearning.entity.Quizz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizzRepository extends JpaRepository<Quizz,Long> {
    List<Quizz> findAllByLessonId(long lessonId);
    boolean existsByLessonId(long lessonId);
}
