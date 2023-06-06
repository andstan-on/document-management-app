package com.springframework.documentmanagementapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springframework.documentmanagementapp.model.UserDTO;
import com.springframework.documentmanagementapp.services.UserService;
import com.springframework.documentmanagementapp.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    UserServiceImpl userServiceImpl;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<UserDTO> userArgumentCaptor;

    @BeforeEach
    void setUp() {
        userServiceImpl = new UserServiceImpl();
    }

    @Test
    void deleteById() throws Exception {
        UserDTO user = userServiceImpl.listUsers().get(0);

        given(userService.deleteUserById(any())).willReturn(true);

        mockMvc.perform(delete(UserController.USER_PATH_ID, user.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(userService).deleteUserById(uuidArgumentCaptor.capture());
        assertThat(user.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void updateById() throws Exception {
        UserDTO user = userServiceImpl.listUsers().get(0);

        given(userService.updateUserById(any(), any())).willReturn(Optional.of(user));

        mockMvc.perform(put(UserController.USER_PATH_ID, user.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isNoContent());

        verify(userService).updateUserById(any(UUID.class), any(UserDTO.class));

    }

    @Test
    void handlePost() throws Exception {
        UserDTO user = userServiceImpl.listUsers().get(0);
        user.setId(null);

        given(userService.saveNewUser(any(UserDTO.class))).willReturn(userServiceImpl.listUsers().get(1));

        mockMvc.perform(post(UserController.USER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void listUsers() throws Exception {
        given(userService.listUsers()).willReturn(userServiceImpl.listUsers());

        mockMvc.perform(get(UserController.USER_PATH)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void getUserByIdNotFound() throws Exception {
        given(userService.getUserById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(UserController.USER_PATH_ID, UUID.randomUUID())).andExpect(status().isNotFound());
    }

    @Test
    void getUserById() throws Exception {
        UserDTO user = userServiceImpl.listUsers().get(0);

        given(userService.getUserById(user.getId())).willReturn(Optional.of(user));

        mockMvc.perform(get(UserController.USER_PATH_ID, user.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId().toString())))
                .andExpect(jsonPath("$.username", is(user.getUsername())));
    }
}