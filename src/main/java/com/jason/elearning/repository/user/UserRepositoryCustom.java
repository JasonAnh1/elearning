package com.jason.elearning.repository.user;

import com.jason.elearning.entity.User;
import com.jason.elearning.entity.constants.UserActive;

import java.util.List;

public interface UserRepositoryCustom {
    List<User> getListUser(int page, String phone, String name, boolean deleted);
    List<User> getListUser2();
    Long countListUser(String phone,String name,boolean deleted);
    List<User> getListUserForOrg(String name);
    List<User> getListOrganizations(UserActive active, String name);
    long countOrganizationMember(Long id);
}
