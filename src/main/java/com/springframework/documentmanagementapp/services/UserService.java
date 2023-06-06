package com.springframework.documentmanagementapp.services;

import com.springframework.documentmanagementapp.model.UserDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    List<UserDTO> listUsers();

    Optional<UserDTO> getUserById(UUID id);

    UserDTO saveNewUser(UserDTO user);

    Optional<UserDTO> updateUserById(UUID existingId, UserDTO user);

    Boolean deleteUserById(UUID userId);
}
