package com.task.services;

import java.util.*;

import com.task.entities.*;
import com.task.repositories.CommentRepository;
import com.task.repositories.PostRepository;
import com.task.repositories.UserRepository; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service 
public class CommentService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentRepository commentRepository;
    public List<Comment> getComments(Long postId){
        Optional<Post> post=postRepository.findById(postId);
        if(post.isPresent()){ 
            List<Comment> comments=new ArrayList<>(post.get().getComments());
            if(comments.size()>1)
                comments.sort((first,second)->first.getDate().compareTo(second.getDate()));
            return comments;
        }
        return Collections.emptyList();
    }

    public Comment addComment(Long postId,Comment comment,Long userId){
        Optional<User> publisher=userRepository.findById(userId);
        Optional<Post> post=postRepository.findById(postId);
        if(!publisher.isPresent()||!post.isPresent())
            return null;
        comment.setPublisher(publisher.get());
        post.get().addComment(comment);
        comment=commentRepository.save(comment);
        return comment;
    }
}
