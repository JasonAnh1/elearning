package com.jason.elearning.service.user;

import com.jason.elearning.entity.Enroll;
import com.jason.elearning.entity.Plan;
import com.jason.elearning.entity.PlanCourse;
import com.jason.elearning.entity.User;
import com.jason.elearning.entity.constants.UserActive;
import com.jason.elearning.entity.request.EnrollRequest;
import com.jason.elearning.entity.request.PlanCourseRequest;

import java.util.List;

public interface UserService {

    /////////////////////User///////////
    void test() throws Exception;
    User signin(User request) throws Exception;

    User signup(User request) throws Exception;

    User signupaslecture(User user) throws Exception;

    User signUpAsLearner(User user) throws Exception;

    User signUpAsOrganization(User user) throws Exception;

    User getUserInfo(Long userId) throws Exception;

    User updateProfile(User request) throws Exception;

    List<Enroll> enrollACourse(EnrollRequest request) throws Exception;

    User verifyLecture(long id) throws Exception;

    /////////////////////Admin///////////
    User adminAddUser(User request) throws Exception;

    User adminSignin(User request) throws Exception;
    List<User> getListUser2()throws Exception;
    List<User> getListUser(int page, String phone, String name, boolean deleted)throws Exception;
    Long countListUser(int page, String phone, String name, boolean deleted)throws Exception;
    User addToOrganization(User request) throws Exception;
    List<User> listOrganizationMember(Long id) throws Exception;
    List<User> getListUserForOrg(String name) throws Exception;
    List<User> listOrganizations(UserActive active,String name) throws Exception;
    List<PlanCourse> addPlanCourse(PlanCourseRequest request) throws Exception;
    Plan getPlan() throws Exception;
    User removeMember(Long userId) throws Exception;
    List<User> getListLecturer(String name) throws Exception;
}
