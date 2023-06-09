package com.springframework.documentmanagementapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springframework.documentmanagementapp.entities.User;
import com.springframework.documentmanagementapp.mappers.UserMapper;
import com.springframework.documentmanagementapp.model.UserDTO;
import com.springframework.documentmanagementapp.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class UserControllerIT {

    @Autowired
    UserController userController;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void testUpdateByIdBadName() throws Exception {
        User user = userRepository.findAll().get(0);

       Map<String, Object> userMap = new HashMap<>();
       userMap.put("username", "NewName123NewName123NewName123NewName123NewName123NewName123NewName123NewName123NewName123NewName123NewName123NewName123NewName123");
       userMap.put("email", "abc@yahoo.com");
       userMap.put("dateOfBirth", "2000-06-08");

       System.out.println(objectMapper.writeValueAsString(userMap));

        MvcResult result = mockMvc.perform(put(UserController.USER_PATH_ID, user.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userMap)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)))
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    void testDeleteByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
           userController.deleteById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    void testDeleteById() {
        User user = userRepository.findAll().get(0);

        ResponseEntity responseEntity = userController.deleteById(user.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        assertThat(userRepository.findById(user.getId()).isEmpty());
    }

    @Test
    void testUpdateNotFound() {
        assertThrows(NotFoundException.class, () -> {
           userController.updateById(UUID.randomUUID(), UserDTO.builder().build());
        });
    }

    @Rollback
    @Transactional
    @Test
    void updateExistingUser() {
        User user = userRepository.findAll().get(0);

        UserDTO userDTO = userMapper.userToUserDto(user);
        userDTO.setId(null);
        final String username = "Updated";
        userDTO.setUsername(username);

        ResponseEntity responseEntity = userController.updateById(user.getId(), userDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        User updatedUser = userRepository.findById(user.getId()).get();
        assertThat(updatedUser.getUsername()).isEqualTo(username);
    }

    @Rollback
    @Transactional
    @Test
    void testSaveUser() {
        UserDTO userDTO = UserDTO.builder()
                .username("user11")
                .build();

        ResponseEntity responseEntity = userController.handlePost(userDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] location = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(location[4]);

        User user = userRepository.findById(savedUUID).get();
        assertThat(user).isNotNull();

    }

    @Rollback
    @Transactional
    @Test
    void testGetByIdNotFound() {
        userRepository.deleteAll();

        assertThrows(NotFoundException.class, () -> {
            userController.getUserById(UUID.randomUUID());
        });
    }

    @Test
    void testGetById() {
        User user = userRepository.findAll().get(0);

        UserDTO userDTO = userController.getUserById(user.getId());

        assertThat(userDTO).isNotNull();
    }

    @Test
    void testListUsers() {

        List<UserDTO> dtos = userController.listUsers();

        assertThat(dtos.size()).isEqualTo(3);

    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList() {
        userRepository.deleteAll();

        assertThat(userController.listUsers().size()).isEqualTo(0);
    }

}