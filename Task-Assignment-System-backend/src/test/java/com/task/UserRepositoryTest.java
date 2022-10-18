package com.task;

import com.task.entities.*;
import com.task.repositories.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import org.apache.commons.codec.digest.DigestUtils;
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
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(1)
    void sizeTest() {
        List<User> list =(List<User>) userRepository.findAll();	
        Assertions.assertThat(list).isNotEmpty();
    }

    @Test
    @Order(2)
    @Rollback(value = false)
    void addOneUserTest() {
        User user = new User();
        user.setEmail("a1@gmail.com");
        user.setRole("student");
        user.setFirstName("Harsha");
        user.setLastName("Ramesh");
        user.setBirthDate(LocalDate.of(1997, Month.JANUARY, 19));
        user.setSex("m");
        user.setPassword(DigestUtils.sha256Hex("password"));
        userRepository.save(user);
        Assertions.assertThat(user.getId()).isGreaterThan(18010000L);
    }

    @Test
    @Order(3)
    @Rollback(value = false)
    void addAnotherUserTest() {
        User user = new User();
        user.setEmail("b1@gmail.com");
        user.setRole("student");
        user.setFirstName("Nithin");
        user.setLastName("Chollangi");
        user.setBirthDate(LocalDate.of(1993, Month.APRIL, 11));
        user.setSex("f");
        user.setPassword(DigestUtils.sha256Hex("pass"));
        User i =userRepository.save(user);
        assertThat(i).isNotNull();
        
    }



}

