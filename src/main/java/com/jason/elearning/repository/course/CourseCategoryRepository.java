package com.jason.elearning.repository.course;

import com.jason.elearning.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseCategoryRepository extends JpaRepository<Category,Long> {
}
