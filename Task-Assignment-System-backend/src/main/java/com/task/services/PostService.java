package com.task.services;

import java.util.*; 
import java.util.stream.Collectors;

import com.task.entities.*;
import com.task.repositories.CourseRepository;
import com.task.repositories.PostRepository;
import com.task.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    PostRepository postRepository;

    public List<Post> getPostsByType(String courseCode, String type, String userId) {
        Optional<Course> course = courseRepository.findByCode(courseCode);
        Optional<User> user = userRepository.findById(Long.valueOf(userId));
        if (!course.isPresent() || !user.isPresent())
            return Collections.emptyList();

        if (course.get().getMembers().contains(user.get())) {
            List<Post> posts = course.get().getPosts()
                    .stream().filter(post -> post.getType().equals(type)).collect(Collectors.toList());

            if (posts.size() > 1)
                posts.sort((first, second) -> second.getDate().compareTo(first.getDate()));
            return posts;
        }
        return Collections.emptyList();
    }

    public Post addPost(String courseCode, Post post, Long userId) {
        Optional<User> publisher = userRepository.findById(userId);
        Optional<Course> course = courseRepository.findByCode(courseCode);
        if (!publisher.isPresent() || !course.isPresent())
            return null;
        if (course.get().getMembers().contains(publisher.get())) {
            post.setPublisher(publisher.get());
            course.get().addPost(post);
            post = postRepository.save(post);
            return post;
        }
        return null;
    }
}

