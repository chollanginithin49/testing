package com.task;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.*;
import java.util.*;

import com.task.services.*;
import com.task.entities.*;
import com.task.repositories.*;

import org.apache.commons.codec.digest.DigestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CommentServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private CommentRepository commentRepository;

    @Autowired
    @InjectMocks
    private CommentService commentService;

    private List<User> userList = new ArrayList<>();
    private List<Course> courseList =  new ArrayList<>();
    private List<Post> postList =  new ArrayList<>();
    private List<Comment> commentList =  new ArrayList<>();


    public void addUsers(){
        User user = new User();
        user.setEmail("a1@gmail.com");
        user.setRole("student");
        user.setFirstName("Harsha");
        user.setLastName("Ramesh");
        user.setId(0L);
        user.setBirthDate(LocalDate.of(1997, Month.JANUARY, 19));
        user.setSex("m");
        user.setPassword(DigestUtils.sha256Hex("password"));

        userList.add(user);

        user = new User();
        user.setEmail("b2@gmail.com");
        user.setRole("student");
        user.setFirstName("Nithin");
        user.setLastName("Chollangi");
        user.setId(1L);
        user.setBirthDate(LocalDate.of(1993, Month.APRIL, 11));
        user.setSex("f");
        user.setPassword(DigestUtils.sha256Hex("pass"));

        userList.add(user);

        user = new User();
        user.setEmail("c1@gmail.com");
        user.setRole("teacher");
        user.setFirstName("Rajesh");
        user.setLastName("Giri");
        user.setId(2L);
        user.setBirthDate(LocalDate.of(1987, Month.SEPTEMBER, 11));
        user.setSex("f");
        user.setPassword(DigestUtils.sha256Hex("NOW_pass"));
        userList.add(user);
    }

    public void addCourses(){
        Course course = new Course();
        course.setCode("CSE 01");
        course.setName("Computer Architecture");
        course.setPosts(new HashSet<>());

        courseList.add(course);

        course=new Course();
        course.setCode("EED 02");
        course.setName("Electronics and Engineering");
        course.setPosts(new HashSet<>());
        courseList.add(course);
    }

    public void addPosts(){
        Post post = new Post();
        post.setTitle("Title1");
        post.setBody("Body1");
        post.setId(0L);
        post.setComments(new HashSet<>());
        post.setPublisher(userList.get(0));
        courseList.get(0).addPost(post);
    
        postList.add(post);

        post = new Post();
        post.setTitle("Title2");
        post.setBody("Body2");
        post.setId(1L);
        post.setComments(new HashSet<>());
        post.setPublisher(userList.get(1));
        courseList.get(0).addPost(post);
        postList.add(post);

        post = new Post();
        post.setTitle("Title3");
        post.setBody("Body3");
        post.setId(2L);
        post.setComments(new HashSet<>());
        post.setPublisher(userList.get(2));
        courseList.get(0).addPost(post);
        postList.add(post);
    }


    
    public void addComments(){
        Comment comment = new Comment();
        comment.setBody("Comment Body1");
        comment.setPublisher(userList.get(0));
        comment.setId(0L);
        comment.setDate(LocalDateTime.now());
        postList.get(0).addComment(comment);
        comment.setPost(postList.get(0));
        commentList.add(comment);

        comment = new Comment();
        comment.setBody("Comment Body2");
        comment.setPublisher(userList.get(2));
        comment.setId(1L);
        comment.setDate(LocalDateTime.now());
        postList.get(0).addComment(comment);
        comment.setPost(postList.get(0));
        commentList.add(comment);

        comment = new Comment();
        comment.setBody("Comment Body3");
        comment.setPublisher(userList.get(1));
        comment.setId(2L);
        comment.setDate(LocalDateTime.now());
        postList.get(1).addComment(comment);
        comment.setPost(postList.get(1));
        commentList.add(comment);
    }


    @Test
    @Order(1)
    void getCommentsCorrectPostTest(){
        addUsers();
        addCourses();
        addPosts();
        addComments();
        Post post = postList.get(0);
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        List<Comment> returnedComments = commentService.getComments(post.getId());
        Assertions.assertThat(returnedComments).isNotNull().hasSize(2);

    }



    @Test
    @Order(2)
    void addCommentTest(){
        addUsers();
        addCourses();
        addPosts();
        addComments();
        Post post = postList.get(0);
        User publisher = userList.get(2);
        Comment comment = commentList.get(0);
        when(userRepository.findById(publisher.getId())).thenReturn(Optional.of(publisher));
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(commentRepository.save(comment)).thenReturn(comment);
        Assertions.assertThat(commentService.addComment(post.getId(), comment, publisher.getId())).isEqualTo(comment);

    }

    
    @Test
    @Order(3)
    void addCommentNoPostTest(){
        addUsers();
        addCourses();
        addPosts();
        addComments();
        User publisher = userList.get(2);
        Comment comment = commentList.get(0);
        when(userRepository.findById(publisher.getId())).thenReturn(Optional.of(publisher));
        when(postRepository.findById(any())).thenReturn(Optional.ofNullable(null));
        Assertions.assertThat(commentService.addComment(0L, comment, publisher.getId())).isNull();

    }

    @Test
    @Order(4)
    void addCommentNoPublisherTest(){
        addUsers();
        addCourses();
        addPosts();
        addComments();
        Post post = postList.get(0);
        Comment comment = commentList.get(0);
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(null));
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        Assertions.assertThat(commentService.addComment(post.getId(), comment, 0L)).isNull();

    }



}
