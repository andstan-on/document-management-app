package com.springframework.documentmanagementapp.repositories;

import com.springframework.documentmanagementapp.entities.User;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void testSaveUserNameTooLong() {
        assertThrows(ConstraintViolationException.class, () -> {
            User savedUser = userRepository.save(User.builder()
                    .username("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                    .dateOfBirth(LocalDate.now())
                    .email("SSADADA")
                    .build());

            userRepository.flush();

        });
    }

    @Test
    void testSaveUser() {
        User user = userRepository.save(User.builder()
                .username("username1")
                .email("abc@yahoo.com")
                .dateOfBirth(LocalDate.now())
                .build());

        userRepository.flush();

        assertThat(user.getId()).isNotNull();
    }
}