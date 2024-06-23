package com.jason.elearning.repository.user;

import com.jason.elearning.entity.User;
import com.jason.elearning.entity.constants.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>,UserRepositoryCustom {
    Optional<User> findByPhone(String phone);

    User findUserById(long id);

    Boolean existsByEmail(String email);

    Boolean existsByPhone(String phone);

    Boolean existsByName(String name);

    List<User> findALlByOrganizationId(Long id);

    User findFirstByOrganizationIdAndId(Long organizationId, Long userId);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<User> findUsersByRoleName(@Param("roleName") RoleName roleName);
}
