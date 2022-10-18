package com.task.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.task.entities.Comment;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    
}
