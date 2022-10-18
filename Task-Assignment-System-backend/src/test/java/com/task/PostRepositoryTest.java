package com.task;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import com.task.entities.*;
import com.task.repositories.*;

import org.apache.commons.codec.digest.DigestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;
    @Autowired 
    private CourseRepository courseRepository;
    @Autowired 
    private UserRepository userRepository;

    @BeforeAll
    public static void addCourses(@Autowired CourseRepository courseRepository, @Autowired PostRepository postRepository){
        courseRepository.deleteAll();
        postRepository.deleteAll();
        Course course = new Course();
        course.setCode("CSE 01");
        course.setName("Computer Architecture");
        course.setPosts(new HashSet<>());

        courseRepository.save(course);

        course=new Course();
        course.setCode("EEE 02");
        course.setName("Electronics and Engineering");
        course.setPosts(new HashSet<>());
        courseRepository.save(course);
    }
    @BeforeAll
    public static void addUsers(@Autowired UserRepository userRepository){
        userRepository.deleteAll();
        User user = new User();
        user.setEmail("a1@gmail.com");
        user.setRole("student");
        user.setFirstName("Harsha");
        user.setLastName("Ramesh");
        user.setBirthDate(LocalDate.of(1997, Month.JANUARY, 19));
        user.setSex("m");
        user.setPassword(DigestUtils.sha256Hex("password"));

        userRepository.save(user);

        user = new User();
        user.setEmail("b1@gmail.com");
        user.setRole("student");
        user.setFirstName("Nithin");
        user.setLastName("Chollangi");
        user.setBirthDate(LocalDate.of(1993, Month.APRIL, 11));
        user.setSex("f");
        user.setPassword(DigestUtils.sha256Hex("pass"));

        userRepository.save(user);

        user = new User();
        user.setEmail("c1@gmail.com");
        user.setRole("teacher");
        user.setFirstName("Rajesh");
        user.setLastName("Giri");
        user.setBirthDate(LocalDate.of(1987, Month.SEPTEMBER, 11));
        user.setSex("f");
        user.setPassword(DigestUtils.sha256Hex("NOW_pass"));
        userRepository.save(user);
    }
    @Test 
    @Order(1)
    void injectedComponentsIsNotNull() {
        Assertions.assertThat(postRepository).isNotNull();
        Assertions.assertThat(courseRepository).isNotNull();
    }

    @Test
    @Order(2)
    @Rollback(value = false)
    void addPostToCourseTest(){
        Optional<Course> course = courseRepository.findByCode("CSE 01");
        Post post =new Post();
        post.setTitle("Title");
        post.setBody("Hi I'm testing here");
        Optional<User> publisher= userRepository.findByEmail("a1@gmail.com");
        post.setPublisher(publisher.get());
        post.setType("post");
        course.get().addPost(post);
        post=postRepository.save(post);
        Optional<Course> courseWithPost = courseRepository.findByCode("CSE 01");
        Assertions.assertThat(courseWithPost.get().getPosts()).hasSize(1);

        Iterator<Post> postIter=courseWithPost.get().getPosts().iterator();
        Post upPost=postIter.next();

        Assertions.assertThat(upPost.getId()).isEqualTo(1L);
        Assertions.assertThat(upPost.getTitle()).isEqualTo("Title");
        User postPublisher = upPost.getPublisher();
        Assertions.assertThat(postPublisher.getEmail()).isEqualTo("a1@gmail.com");
    }
    @Test
    @Order(3)
    void checkPostDateAddedAutomatically(){
        Optional<Post> post = postRepository.findById(1L);
        Assertions.assertThat(post.get().getDate()).isNotNull();
    }
    @Test
    @Order(4)
    @Rollback(value = false)
    void addAnotherPostToExistingCourse_ShouldBeAddedCorrectly(){
        Post post = new Post();
        post.setTitle("Second post");
        post.setBody("Body2");
        post.setType("announcment");
        post.setPublisher(userRepository.findByEmail("a1@gmail.com").get());
        Optional<Course> course = courseRepository.findByCode("CSE 01");
        course.get().addPost(post);
        courseRepository.save(course.get());
        // check added
        Optional<Course> courseWithPost=courseRepository.findByCode("CSE 01");
        Assertions.assertThat(courseWithPost.get().getPosts()).hasSize(2);
    }
    @Test
    @Order(5)
    void addPostWithoutPublisher_ShouldThrowException(){
        Post post =new Post();
        post.setTitle("Title");
        post.setBody("Hi I'm testing here");
        post.setType("announcement");
        Optional<Course> course = courseRepository.findByCode("EEE 02");
        course.get().addPost(post);
        Assertions.assertThatThrownBy(() -> {
            postRepository.save(post);
        }).isInstanceOf(DataIntegrityViolationException.class);
    }
    @Test
    @Order(6)
    void addPostWithoutTitle_ShouldThrowException(){
        Post post = new Post();
        post.setBody("Post without title");
        post.setType("announcement");
        post.setPublisher(userRepository.findByEmail("b1@gmail.com").get());
        Course course=courseRepository.findByCode("CSE 01").get();
        course.addPost(post);
        Assertions.assertThatThrownBy(() -> {
            postRepository.save(post);
        }).isInstanceOf(DataIntegrityViolationException.class);
    }    
    @Test
    @Order(7)
    void addPostWithoutBody_ShouldThrowException(){
        Post post = new Post();
        post.setTitle("Post without Body");
        post.setType("announcement");
        post.setPublisher(userRepository.findByEmail("b1@gmail.com").get());
        Course course=courseRepository.findByCode("CSE 01").get();
        course.addPost(post);
        Assertions.assertThatThrownBy(() -> {
            postRepository.save(post);
        }).isInstanceOf(DataIntegrityViolationException.class);
    }
    @Test
    @Order(8)
    void addPostWithoutCourse_ShouldThrowException(){
        Post post = new Post();
        post.setTitle("Post without Body");
        post.setBody("body");
        post.setType("announcement");
        post.setPublisher(userRepository.findByEmail("b1@gmail.com").get());
        Assertions.assertThatThrownBy(() -> {
            postRepository.save(post);
        }).isInstanceOf(DataIntegrityViolationException.class);
    }
}
