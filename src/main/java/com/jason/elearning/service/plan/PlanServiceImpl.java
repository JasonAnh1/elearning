package com.jason.elearning.service.plan;

import com.jason.elearning.configuration.Translator;
import com.jason.elearning.entity.*;
import com.jason.elearning.entity.constants.PlanType;
import com.jason.elearning.entity.constants.RoleName;
import com.jason.elearning.repository.course.CoursePartRepository;
import com.jason.elearning.repository.course.CourseRepository;
import com.jason.elearning.repository.course.LessonRepository;
import com.jason.elearning.repository.plan.PlanCourseRepository;
import com.jason.elearning.repository.plan.PlanRepository;
import com.jason.elearning.repository.user.EnrollRepository;
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

    @Autowired
    private EnrollRepository enrollRepository;

    @Autowired
    private CoursePartRepository coursePartRepository;

    @Autowired
    private LessonRepository lessonRepository;

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

    @Override
    public List<Course> getOrgCourseForMember(Long orgId) throws Exception {
        User user = getUser();
        if (user ==null ) {
            throw new Exception(Translator.toLocale("access_denied"));
        }
        List<Enroll> lstEnrolled = enrollRepository.findByUserId(user.getId());
        List<Course> lstCourseEnrolled = lstEnrolled.stream().map(
                Enroll::getCourse
        ).collect(Collectors.toList());

        Plan plan = planRepository.findFirstByOrganizationIdOrderByCreatedAtDesc(orgId);
        List<Long> listCourseId = planCourseRepository.findAllByPlanId(plan.getId())
                .stream()
                .map(PlanCourse::getCourseID)
                .collect(Collectors.toList());
        List<Course> lstOrgCourses = courseRepository.listByListIds(listCourseId);

        // tra ve course cua org ma user chưa tự enroll
        List<Course> availableCoursesForUser = lstOrgCourses.stream()
                .filter(course -> !lstCourseEnrolled.contains(course))
                .collect(Collectors.toList());

        // set progress
        for(Course course: availableCoursesForUser ){
            setProgress(course,user.getId());
        }

        return availableCoursesForUser;
    }
    private void setProgress(Course course, long id) {
        List<Long> coursePartListId = coursePartRepository.findAllByCourseId(course.getId())
                .stream().map(CoursePart::getId).collect(Collectors.toList());

        List<Long> lessonList = lessonRepository.listLessonByListCoursePartId(coursePartListId)
                .stream().map(Lesson::getId).collect(Collectors.toList());

        List<LessonProgress> lessonProgresses = lessonRepository.listLearningLessonProgress(id,lessonList);
        int lessonDone = 0;
        for(LessonProgress lsProgress: lessonProgresses){
            if(lsProgress.getProgress() == null){
                lsProgress.setProgress(0.0);
            }
            if(lsProgress.getProgress() == 100){
                lessonDone++;
            }
        }
        double total = lessonProgresses.size();
        double courseProgress = lessonDone / total * 100;
        if(total == 0){
            course.setProgress(0);
        }else {
            course.setProgress(courseProgress);
        }

    }
}
