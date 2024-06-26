package com.jason.elearning.repository.user;

import com.jason.elearning.entity.Enroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollRepository extends JpaRepository<Enroll,Long> {
    List<Enroll> findByUserId(Long id);
    List<Enroll> findAllByCourseId(Long courseId);
    Enroll findFirstByUserIdAndCourseId(Long userId,Long courseId);
    Boolean existsByUserIdAndCourseId(Long userId,Long courseId);
}
