package com.task.controllers;

import java.util.*;

import com.task.entities.*;
import com.task.services.*;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = { "http://localhost:8080" },allowCredentials = "true")
@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    PostService postService;
    @Autowired
    CommentService commentService;
    @GetMapping(path="courses")
    public List<Course> getCourses(@CookieValue("id") String id){
        if(id.isEmpty()){
            return Collections.emptyList();
        }
        return userService.getCourses(Long.valueOf(id));
    }

    @GetMapping(path="courses/{courseCode}")
    public List<Post> getPosts(@CookieValue("id") String id,@PathVariable("courseCode") String courseCode, @RequestParam String postType){
        if(id.isEmpty()){
            return Collections.emptyList();
        }
        return postService.getPostsByType(courseCode, postType, id);
    }

    @GetMapping(path="comments/{postId}")
    public List<Comment> getComments(@CookieValue("id") String id,@PathVariable String postId){
        if(id.isEmpty()){
            return Collections.emptyList();
        }
        return commentService.getComments(Long.valueOf(postId));
    }
    
    @PostMapping(path="post")
    public Post addPost(@CookieValue("id") String id, @RequestParam String courseCode,@RequestBody String postString){
        if(id.isEmpty()){
            return null;
        }
        JSONObject postJson = new JSONObject(postString);
        Post post = new Post();
        post.setTitle(postJson.getString("title"));
        post.setType(postJson.getString("type"));
        post.setBody(postJson.getString("body"));
        return postService.addPost(courseCode, post,Long.valueOf(id));
    }
    @PostMapping(path = "comment")
    public Comment addComment(@CookieValue("id") String id,@RequestParam String postId, @RequestBody String commentString){
        if(id.isEmpty())
            return null;
        Comment comment = new Comment();
        comment.setBody(new JSONObject(commentString).getString("body"));
        return commentService.addComment(Long.valueOf(postId),comment,Long.valueOf(id));
    }
}

