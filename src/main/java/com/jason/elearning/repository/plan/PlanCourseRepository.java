package com.jason.elearning.repository.plan;

import com.jason.elearning.entity.PlanCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanCourseRepository extends JpaRepository<PlanCourse,Long> {
    List<PlanCourse> findAllByPlanId(long planId);
}
