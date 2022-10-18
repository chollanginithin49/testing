package com.task.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.task.entities.Course;
@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    Optional<Course> findByCode(String code);
    Optional<Course> findByName(String name);
}