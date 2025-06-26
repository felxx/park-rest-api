package com.felxx.park_rest_api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.felxx.park_rest_api.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("SELECT u.role FROM User u WHERE u.username like :username")
    User.Role findRoleByUsername(String username);
}
