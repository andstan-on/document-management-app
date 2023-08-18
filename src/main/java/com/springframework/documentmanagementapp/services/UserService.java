package com.springframework.documentmanagementapp.services;

import com.springframework.documentmanagementapp.model.UserDTO;
import com.springframework.documentmanagementapp.model.UserRole;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    List<UserDTO> listUsers();

    Page<UserDTO> pageUsers(String searchedCriteria, Integer pageNumber, Integer pageSize);

    Optional<UserDTO> getUserById(UUID id);

    UserDTO saveNewUser(UserDTO user);

    Optional<UserDTO> updateUserById(UUID existingId, UserDTO user);

    Optional<UserDTO> updateUserRole(UUID userId, UserRole userRole);

    Boolean deleteUserById(UUID userId);

    Optional<UserDTO> getLoggedInUser();
}
