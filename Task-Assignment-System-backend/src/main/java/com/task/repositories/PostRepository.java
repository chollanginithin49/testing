package com.task.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.task.entities.Post;

public interface PostRepository extends JpaRepository<Post,Long> {
    
}

