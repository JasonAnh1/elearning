package com.jason.elearning.repository.course;

import com.jason.elearning.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson,Long>, LessonRepositoryCustom {
     Optional<Lesson> findFirstByPart_IdAndPosition(long coursePartId, int position);

}
