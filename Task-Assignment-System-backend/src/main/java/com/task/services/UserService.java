package com.task.services;

import java.util.*;

import com.task.entities.*;
import com.task.repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService { 
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;

    public List<Course> getCourses(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return new ArrayList<>(user.get().getCourses());
        }
        return Collections.emptyList();
    }

    public Course getCourse(String code) {
        Optional<Course> course = courseRepository.findByCode(code);
        if (course.isPresent()) {
            return course.get();
        }
        return null;
    }
}

