package com.jason.elearning.repository.plan;

import com.jason.elearning.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan,Long > {
    Plan findFirstByOrganizationIdOrderByCreatedAtDesc(Long id);
}
