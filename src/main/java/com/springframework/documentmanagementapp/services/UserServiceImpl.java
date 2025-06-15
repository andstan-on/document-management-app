package com.springframework.documentmanagementapp.services;

import com.springframework.documentmanagementapp.model.UserDTO;
import com.springframework.documentmanagementapp.model.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private Map<UUID, UserDTO> userMap;

    public UserServiceImpl(){
        this.userMap = new HashMap<>();

        UserDTO user1 = UserDTO.builder()
                .id(UUID.randomUUID())
                .username("username1")
                .password("123456")
                .firstName("fname1")
                .lastName("lname1")
                .email("email1")
                .dateOfBirth(LocalDate.now())
                .role(UserRole.REGULAR)
                .build();

        UserDTO user2 = UserDTO.builder()
                .id(UUID.randomUUID())
                .username("username2")
                .password("123456")
                .firstName("fname2")
                .lastName("lname2")
                .email("email2")
                .dateOfBirth(LocalDate.now())
                .role(UserRole.REGULAR)
                .build();

        UserDTO user3 = UserDTO.builder()
                .id(UUID.randomUUID())
                .username("username3")
                .password("123456")
                .firstName("fname3")
                .lastName("lname3")
                .email("email3")
                .dateOfBirth(LocalDate.now())
                .role(UserRole.REGULAR)
                .build();

        userMap.put(user1.getId(), user1);
        userMap.put(user2.getId(), user2);
        userMap.put(user3.getId(), user3);
    }

    @Override
    public List<UserDTO> listUsers() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public Optional<UserDTO> getUserById(UUID id) {
        return Optional.ofNullable(userMap.get(id));
    }

    @Override
    public UserDTO saveNewUser(UserDTO user) {
        UserDTO savedUser = UserDTO.builder()
                .id(UUID.randomUUID())
                .username(user.getUsername())
                .password(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .dateOfBirth(user.getDateOfBirth())
                .role(user.getRole())
                .build();

        userMap.put(savedUser.getId(), savedUser);

        return savedUser;
    }

    @Override
    public Optional<UserDTO> updateUserById(UUID existingId, UserDTO user) {
        UserDTO existing = userMap.get(existingId);

        existing.setUsername(user.getUsername());
        existing.setPassword(user.getPassword());
        existing.setFirstName(user.getFirstName());
        existing.setLastName(user.getLastName());
        existing.setEmail(user.getEmail());
        existing.setDateOfBirth(user.getDateOfBirth());
        existing.setRole(user.getRole());

        return Optional.of(existing);

    }

    @Override
    public Boolean deleteUserById(UUID userId) {
        userMap.remove(userId);

        return true;
    }

    @Override
    public Optional<UserDTO> getLoggedInUser() {
        return Optional.empty();
    }
}
