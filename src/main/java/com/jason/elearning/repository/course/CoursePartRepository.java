package com.jason.elearning.repository.course;

import com.jason.elearning.entity.CoursePart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Repository
public interface CoursePartRepository extends JpaRepository<CoursePart,Long> , CoursePartRepositoryCustom{
    List<CoursePart> findAllByCourseId(Long authorId);
}
