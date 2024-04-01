package com.jason.elearning.repository.course;

import com.jason.elearning.entity.CoursePart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoursePartRepository extends JpaRepository<CoursePart,Long> , CoursePartRepositoryCustom{
    List<CoursePart> findAllByCourseId(Long authorId);
    List<CoursePart> findAllByCourseIdOrderByPartNumber(Long authorId);
    Optional<CoursePart> findFirstByCourseIdAndPartNumber(long courseId, int partNumber);
}
