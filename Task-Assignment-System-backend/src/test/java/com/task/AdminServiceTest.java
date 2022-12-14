package com.task;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
class AdminServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private CourseRepository courseRepository;

    @Autowired
    @InjectMocks
    private AdminService adminService;

    private User user1;
    private User user2;
    private User user3;
    private List<User> userList = new ArrayList<>();

    private Course course1;
    private Course course2;
    private List<Course> courseList =  new ArrayList<>();


    
    private void addUsers(){
        user1 = new User();
        user1.setEmail("a@gmail.com");
        user1.setRole("student");
        user1.setFirstName("ahmed");
        user1.setLastName("nagy");
        user1.setBirthDate(LocalDate.of(1997, Month.JANUARY, 19));
        user1.setSex("m");
        user1.setPassword(DigestUtils.sha256Hex("password"));

        user2 = new User();
        user2.setEmail("b@gmail.com");
        user2.setRole("student");
        user2.setFirstName("chloe");
        user2.setLastName("todd");
        user2.setBirthDate(LocalDate.of(1993, Month.APRIL, 11));
        user2.setSex("f");
        user2.setPassword(DigestUtils.sha256Hex("pass"));

        
        user3 = new User();
        user3.setEmail("c@gmail.com");
        user3.setRole("teacher");
        user3.setFirstName("kate");
        user3.setLastName("smith");
        user3.setBirthDate(LocalDate.of(1987, Month.SEPTEMBER, 11));
        user3.setSex("f");
        user3.setPassword(DigestUtils.sha256Hex("NOW_pass"));

        userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);

    }

    private void addCourses(){

        course1 = new Course();
        course1.setCode("CSE 01");
        course1.setName("Computer Architecture");

        course2 = new Course();
        course2.setCode("CSE 02");
        course2.setName("Operating Systems");

        courseList = new ArrayList<>();
        courseList.add(course1);
        courseList.add(course2);
    }



    @Test
    @Order(1)
    void getTeachersTest(){
        addUsers();
        List<User> addedteachers = new ArrayList<>();
        for(User user : userList){
            if(user.getRole().equals("teacher"))
            addedteachers.add(user);
        }
        when(userRepository.findByRole("teacher")).thenReturn(addedteachers);
        List<User> students =  adminService.getTeachers();
        Assertions.assertThat(students.size()).isEqualTo(addedteachers.size()).isEqualTo(1);
    }


    @Test
    @Order(2)
    void getTeachersNoAddTest(){
        List<User> addedTeachers = new ArrayList<>();
        for(User user : userList){
            if(user.getRole().equals("teacher"))
            addedTeachers.add(user);
        }
        when(userRepository.findByRole("teacher")).thenReturn(addedTeachers);
        List<User> students =  adminService.getTeachers();
        Assertions.assertThat(students.size()).isEqualTo(addedTeachers.size()).isEqualTo(0);
    }

    @Test
    @Order(3)
    void getCoursesTest(){
        addCourses();
        when(courseRepository.findAll()).thenReturn(courseList);
        List<Course> courses =  adminService.getCourses();
        Assertions.assertThat(courses.size()).isEqualTo(courseList.size()).isEqualTo(2);
    }

    @Test
    @Order(4)
    void getCoursesNoAddTest(){
        when(courseRepository.findAll()).thenReturn(courseList);
        List<Course> courses =  adminService.getCourses();
        Assertions.assertThat(courses.size()).isEqualTo(courseList.size()).isEqualTo(0);
    }

    @Test
    @Order(5)
    void addUserTest(){
        addUsers();
        User user = userList.get(0);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.ofNullable(null));
        user.setId(1801001L);
        Assertions.assertThat(adminService.addUser(user)).isNotNull();
        verify(userRepository,times(1)).save(user);
    }

    @Test
    @Order(6)
    void addExistingUserTest(){
        addUsers();
        User user = userList.get(0);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.ofNullable(user));
        Assertions.assertThat(adminService.addUser(user)).isZero();
        verify(userRepository,times(0)).save(any());

    }

    @Test
    @Order(7)
    void changePasswordNewUserTest(){
        addUsers();
        when(userRepository.findById(125L)).thenReturn(Optional.ofNullable(null));
        Assertions.assertThat(adminService.changePassword(125L, "totalyNewPassword")).isFalse();
        verify(userRepository,times(0)).save(any());
    }


    @Test
    @Order(8)
    void addCourseTest(){
        addCourses();
        Course course = courseList.get(0);
        when(courseRepository.findByCode(course.getCode())).thenReturn(Optional.ofNullable(null));
        Assertions.assertThat(adminService.addCourse(course.getCode(), course.getName())).isTrue();
        verify(courseRepository,times(1)).save(course);
    }

    @Test
    @Order(9)
    void addExistingCourseTest(){
        addCourses();
        Course course = courseList.get(0);
        when(courseRepository.findByCode(course.getCode())).thenReturn(Optional.ofNullable(course));
        Assertions.assertThat(adminService.addCourse(course.getCode(), course.getName())).isFalse();
        verify(courseRepository,times(0)).save(any());
    }

    @Test
    @Order(10)
    void getCourseTest(){
        addCourses();
        Course course = courseList.get(0);
        when(courseRepository.findByCode(course.getCode())).thenReturn(Optional.ofNullable(course));
        Assertions.assertThat(adminService.getCourse(course.getCode())).isNotNull();
    }

    @Test
    @Order(11)
    void getNonExistingCourseTest(){
        addCourses();
        Course course = courseList.get(0);
        when(courseRepository.findByCode(course.getCode())).thenReturn(Optional.ofNullable(null));
        Assertions.assertThat(adminService.getCourse(course.getCode())).isNull();
    }

    @Test
    @Order(12)
    void addUserToCourseTest(){
        addUsers();
        addCourses();

        User user = userList.get(0);
        when(userRepository.findById(1801001L)).thenReturn(Optional.ofNullable(user));
        
        Course course = courseList.get(0);
        when(courseRepository.findByCode(course.getCode())).thenReturn(Optional.ofNullable(course));

        Assertions.assertThat(adminService.addUserToCourse(1801001L, course.getCode() ,user.getRole())).isNotNull();
        Assertions.assertThat(user.getCourses()).hasSize(1);
        Assertions.assertThat(course.getMembers()).hasSize(1);
        
    }

    @Test
    @Order(13)
    void addNonExistingUserToCourseTest(){
        addUsers();
        addCourses();

        User user = userList.get(0);
        when(userRepository.findById(1801001L)).thenReturn(Optional.ofNullable(null));
        
        Course course = courseList.get(0);
        when(courseRepository.findByCode(course.getCode())).thenReturn(Optional.ofNullable(course));

        Assertions.assertThat(adminService.addUserToCourse(1801001L, course.getCode(), user.getRole())).isNull();
        Assertions.assertThat(user.getCourses()).isEmpty();
        Assertions.assertThat(course.getMembers()).isEmpty();
        
    }

    @Test
    @Order(14)
    void addUserToNonExistingCourseTest(){
        addUsers();
        addCourses();

        User user = userList.get(0);
        when(userRepository.findById(1801001L)).thenReturn(Optional.ofNullable(user));
        
        Course course = courseList.get(0);
        when(courseRepository.findByCode(course.getCode())).thenReturn(Optional.ofNullable(null));

        Assertions.assertThat(adminService.addUserToCourse(1801001L, course.getCode(), user.getRole())).isNull();
        Assertions.assertThat(user.getCourses()).isEmpty();
        Assertions.assertThat(course.getMembers()).isEmpty();
        
    }

    @Test
    @Order(15)
    void removeUserFromCourseTest(){
        addUsers();
        addCourses();

        User user = userList.get(0);
        when(userRepository.findById(1801001L)).thenReturn(Optional.ofNullable(user));
        
        Course course = courseList.get(0);
        when(courseRepository.findByCode(course.getCode())).thenReturn(Optional.ofNullable(course));

        Assertions.assertThat(adminService.addUserToCourse(1801001L, course.getCode(), user.getRole())).isNotNull();
        Assertions.assertThat(user.getCourses()).hasSize(1);
        Assertions.assertThat(course.getMembers()).hasSize(1);


        Assertions.assertThat(adminService.removeUserFromCourse(1801001L, course.getCode())).isTrue();
        Assertions.assertThat(user.getCourses()).isEmpty();
        Assertions.assertThat(course.getMembers()).isEmpty();
    }

    @Test
    @Order(16)
    void removeNonAddedUserFromCourseTest(){
        addUsers();
        addCourses();

        User user = userList.get(0);
        when(userRepository.findById(1801001L)).thenReturn(Optional.ofNullable(user));
        
        Course course = courseList.get(0);
        when(courseRepository.findByCode(course.getCode())).thenReturn(Optional.ofNullable(course));

        Assertions.assertThat(user.getCourses()).isEmpty();
        Assertions.assertThat(course.getMembers()).isEmpty();
        Assertions.assertThat(adminService.removeUserFromCourse(1801001L, course.getCode())).isFalse();
        Assertions.assertThat(user.getCourses()).isEmpty();
        Assertions.assertThat(course.getMembers()).isEmpty();
    }

    @Test
    @Order(17)
    void deleteUserTest(){
        addUsers();
        User user = userList.get(0);
        when(userRepository.findById(1801001L)).thenReturn(Optional.ofNullable(user));
        Assertions.assertThat(adminService.deleteUser(1801001L)).isTrue();
        verify(userRepository,times(1)).delete(user);
    }

    @Test
    @Order(18)
    void deleteNonExistingUserTest(){
        when(userRepository.findById(1801001L)).thenReturn(Optional.ofNullable(null));
        Assertions.assertThat(adminService.deleteUser(1801001L)).isFalse();
        verify(userRepository,times(0)).delete(any());
    }

    @Test
    @Order(19)
    void deleteCourseTest(){
        addCourses();
        Course course = courseList.get(0);
        when(courseRepository.findByCode(course.getCode())).thenReturn(Optional.ofNullable(course));
        Assertions.assertThat(adminService.deleteCourse(course.getCode())).isTrue();
        verify(courseRepository,times(1)).delete(course);
    }

    @Test
    @Order(20)
    void deleteNonExistingCourseTest(){
        addCourses();
        Course course = courseList.get(0);
        when(courseRepository.findByCode(course.getCode())).thenReturn(Optional.ofNullable(null));
        Assertions.assertThat(adminService.deleteCourse(course.getCode())).isFalse();
        verify(courseRepository,times(0)).delete(any());
    }

    @Test
    @Order(21)
    void getCourseMembersTest(){
        addUsers();
        addCourses();
        when(userRepository.findById(1801001L)).thenReturn(Optional.ofNullable(user1));
        when(userRepository.findById(1801002L)).thenReturn(Optional.ofNullable(user2));
        when(userRepository.findById(1801003L)).thenReturn(Optional.ofNullable(user3));

        when(courseRepository.findByCode(course1.getCode())).thenReturn(Optional.ofNullable(course1));
        when(courseRepository.findByCode(course2.getCode())).thenReturn(Optional.ofNullable(course2));

        adminService.addUserToCourse(1801001L, course1.getCode(), user1.getRole());
        adminService.addUserToCourse(1801002L, course1.getCode(), user2.getRole());

        adminService.addUserToCourse(1801002L, course2.getCode(), user2.getRole());
        adminService.addUserToCourse(1801003L, course2.getCode(), user3.getRole());

        Assertions.assertThat(course1.getMembers()).hasSize(2);
        Assertions.assertThat(course2.getMembers()).hasSize(2);
        Assertions.assertThat(user1.getCourses()).hasSize(1);
        Assertions.assertThat(user2.getCourses()).hasSize(2);
        Assertions.assertThat(user3.getCourses()).hasSize(1);

        Assertions.assertThat(adminService.getCourseMembers(course1.getCode(), "student")).containsExactlyInAnyOrder(user1, user2);
        Assertions.assertThat(adminService.getCourseMembers(course1.getCode(), "teacher")).isEqualTo(Arrays.asList());
        Assertions.assertThat(adminService.getCourseMembers(course2.getCode(), "student")).isEqualTo(Arrays.asList(user2));
        Assertions.assertThat(adminService.getCourseMembers(course2.getCode(), "teacher")).isEqualTo(Arrays.asList(user3));
    }

    @Test
    @Order(22)
    void updateCourseNameTest(){
        addCourses();
        Assertions.assertThat(course1.getName()).isEqualTo("Computer Architecture");
        when(courseRepository.findByCode(course1.getCode())).thenReturn(Optional.ofNullable(course1));
        Assertions.assertThat(adminService.updateCourseName(course1.getCode(), "NewName")).isTrue();
        Assertions.assertThat(course1.getName()).isEqualTo("NewName");
    }
    @Test
    @Order(23)
    void updateNonExistingCourseNameTest(){
        addCourses();
        when(courseRepository.findByCode("NON EXISTING CODE")).thenReturn(Optional.ofNullable(null));
        Assertions.assertThat(adminService.updateCourseName("NON EXISTING CODE", "NewName")).isFalse();
    }
    
}

