package com.jason.elearning.service.user;

import com.jason.elearning.entity.Enroll;
import com.jason.elearning.entity.User;
import com.jason.elearning.entity.request.EnrollRequest;

import java.util.List;

public interface UserService {

    /////////////////////User///////////
    void test() throws Exception;
    User signin(User request) throws Exception;

    User signup(User request) throws Exception;

    User getUserInfo(Long userId) throws Exception;

    User updateProfile(User request) throws Exception;

    List<Enroll> enrollACourse(EnrollRequest request) throws Exception;


    /////////////////////Admin///////////
    User adminAddUser(User request) throws Exception;

    User adminSignin(User request) throws Exception;
    List<User> getListUser2()throws Exception;
    List<User> getListUser(int page, String phone, String name, boolean deleted)throws Exception;
    Long countListUser(int page, String phone, String name, boolean deleted)throws Exception;

}
