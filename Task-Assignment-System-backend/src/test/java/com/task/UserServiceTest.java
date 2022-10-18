package com.task;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.*;
import java.util.*;

import com.task.services.*;
import com.task.entities.*;
import com.task.repositories.*;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private CourseRepository courseRepository;

    @Autowired
    @InjectMocks
    private UserService userService;

    private List<User> userList = new ArrayList<>();
    private List<Course> courseList =  new ArrayList<>();


    
    private void addUsers(){
        userList = new ArrayList<>();
        User user;
        user = new User();
        user.setEmail("a1@gmail.com");
        user.setRole("student");
        user.setFirstName("Harsha");
        user.setLastName("Ramesh");
        user.setBirthDate(LocalDate.of(1997, Month.JANUARY, 19));
        user.setSex("m");
        user.setPassword(DigestUtils.sha256Hex("password"));
        userList.add(user);

        user = new User();
        user.setEmail("b1@gmail.com");
        user.setRole("teacher");
        user.setFirstName("Nithin");
        user.setLastName("Chollangi");
        user.setBirthDate(LocalDate.of(1993, Month.APRIL, 11));
        user.setSex("m");
        user.setPassword(DigestUtils.sha256Hex("pass"));
        userList.add(user);
        
        user = new User();
        user.setEmail("c1@gmail.com");
        user.setRole("student");
        user.setFirstName("Rajesh");
        user.setLastName("Giri");
        user.setBirthDate(LocalDate.of(1987, Month.SEPTEMBER, 11));
        user.setSex("m");
        user.setPassword(DigestUtils.sha256Hex("NOW_pass"));
        userList.add(user);
    }

    private void addCourses(){
        courseList = new ArrayList<>();
        Course course;
        course = new Course();
        course.setCode("CSE 01");
        course.setName("Computer Architecture");
        courseList.add(course);

        course = new Course();
        course.setCode("CSE 02");
        course.setName("Opearating Systems");
        courseList.add(course);
    }
    private void addUsersToCourses(){

        courseList.get(0).addUser(userList.get(0));
        courseList.get(1).addUser(userList.get(0));
        courseList.get(0).addUser(userList.get(1));
    }
    void prepareDB(){
        addUsers();
        addCourses();
        addUsersToCourses();
    }
    @Test
    void getCoursesOfUserInASingleCourse(){
        prepareDB();
        User user = userList.get(1);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        List<Course> courses = userService.getCourses(user.getId());
        assertNotNull(courses);
        assertEquals(1, courses.size());
        assertEquals(courseList.get(0).getCode(), courses.get(0).getCode());
    }


    @Test
    void getCoursesOfUserNotInAnyCourse(){
        prepareDB();
        User user = userList.get(2);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        List<Course> courses = userService.getCourses(user.getId());
        assertEquals(0, courses.size());
    }

    @Test
    void getCourseWithCodeInDB(){
        addCourses();
        when(courseRepository.findByCode("CSE 01")).thenReturn(Optional.of(courseList.get(0)));
        Course course = userService.getCourse("CSE 01");
        assertEquals("Computer Architecture", course.getName());
    }
    @Test
    void getCourseWithCodeNotInDB(){
        addCourses();
        when(courseRepository.findByCode("CSE 03")).thenReturn(Optional.ofNullable(null));
        Course course = userService.getCourse("CSE 03");
        assertNull(course);
    }
}

