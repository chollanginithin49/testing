package com.task;

import com.task.entities.*;
import com.task.repositories.CourseRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;


@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    @Order(1)
    @Rollback(value = false)
    void addOneCourseTest() {
        courseRepository.deleteAll();
        Course course = new Course();
        course.setCode("CSE 01");
        course.setName("Computer Architecture");
        courseRepository.save(course);

        Assertions.assertThat(courseRepository.findByCode("CSE 01").get().getName()).isEqualTo("Computer Architecture");
    }
}

