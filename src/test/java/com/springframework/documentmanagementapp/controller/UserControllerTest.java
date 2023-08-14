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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;
import java.util.UUID;
import jakarta.servlet.Filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
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
    void testUpdateUserBlankEmail() throws Exception {
        UserDTO userDTO = userServiceImpl.listUsers().get(0);
        userDTO.setEmail(" ");

        System.out.println(userDTO.getUsername());

        given(userService.updateUserById(any(UUID.class), any(UserDTO.class))).willReturn(Optional.of(userDTO));

        MvcResult mvcResult = mockMvc.perform(put(UserController.USER_PATH_ID, userDTO.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(status().isBadRequest()).andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());

    }

    @Test
    void testCreateUserNullUsername() throws Exception{
        UserDTO userDTO = UserDTO.builder().build();

        given(userService.saveNewUser(any(UserDTO.class))).willReturn(userServiceImpl.listUsers().get(1));

        MvcResult mvcResult = mockMvc.perform(post(UserController.USER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(jsonPath("$.length()", is(5)))
                .andExpect(status().isBadRequest()).andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
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