package com.jason.elearning.service.plan;

import com.jason.elearning.configuration.Translator;
import com.jason.elearning.entity.Course;
import com.jason.elearning.entity.Plan;
import com.jason.elearning.entity.PlanCourse;
import com.jason.elearning.entity.User;
import com.jason.elearning.entity.constants.PlanType;
import com.jason.elearning.entity.constants.RoleName;
import com.jason.elearning.repository.course.CourseRepository;
import com.jason.elearning.repository.plan.PlanCourseRepository;
import com.jason.elearning.repository.plan.PlanRepository;
import com.jason.elearning.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanServiceImpl extends BaseService implements PlanService{

    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private PlanCourseRepository planCourseRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Override
    public Plan newPlaneContact(PlanType planType) throws Exception {
        User user = getUser();
        if (user ==null || user.getRoles().get(0).getName() != RoleName.ROLE_ORGANIZATION   ) {
            throw new Exception(Translator.toLocale("access_denied"));
        }
        Plan plan = new Plan();
        plan.setOrganizationId(user.getId());
        plan.setType(planType);
        return planRepository.save(plan);
    }

    @Override
    public List<Course> getOrgCourse(Long orgId) throws Exception {
        Plan plan = planRepository.findFirstByOrganizationIdOrderByCreatedAtDesc(orgId);
        List<Long> listCourseId = planCourseRepository.findAllByPlanId(plan.getId())
                                    .stream()
                                    .map(PlanCourse::getCourseID)
                                    .collect(Collectors.toList());

        return courseRepository.listByListIds(listCourseId);
    }
}
