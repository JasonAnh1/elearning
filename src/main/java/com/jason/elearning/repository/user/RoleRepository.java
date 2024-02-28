package com.jason.elearning.repository.user;

import com.jason.elearning.entity.Role;
import com.jason.elearning.entity.constants.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByTitle(String roleName);

    Optional<Role> findByName(RoleName roleName);


}
