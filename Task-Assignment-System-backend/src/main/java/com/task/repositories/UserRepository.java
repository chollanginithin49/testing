package com.task.repositories;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.task.entities.User;
@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    List<User> findByRole(String role);
    Optional<User> findByEmail(String email);
}
