package com.jason.elearning.service.plan;

import com.jason.elearning.entity.Course;
import com.jason.elearning.entity.Plan;
import com.jason.elearning.entity.constants.PlanType;

import java.util.List;

public interface PlanService {
    Plan newPlaneContact(PlanType planType) throws Exception;
    List<Course> getOrgCourse(Long orgId) throws Exception;

}
