package com.jason.elearning.repository.user;

import com.jason.elearning.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>,UserRepositoryCustom {
    Optional<User> findByPhone(String phone);

    User findUserById(long id);

    Boolean existsByEmail(String email);

    Boolean existsByPhone(String phone);

    Boolean existsByName(String name);
}
