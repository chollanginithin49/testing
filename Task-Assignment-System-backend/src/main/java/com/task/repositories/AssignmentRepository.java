package com.task.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.task.entities.Assignment;

public interface AssignmentRepository extends JpaRepository<Assignment,Long> {
    
}