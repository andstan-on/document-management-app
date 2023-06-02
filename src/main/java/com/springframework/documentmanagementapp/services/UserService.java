package com.springframework.documentmanagementapp.services;

import com.springframework.documentmanagementapp.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    List<User> listUsers();

    Optional<User> getUserById(UUID id);

    User saveNewUser(User user);

    void updateUserById(UUID existingId, User user);

    void deleteUserById(UUID userId);
}
