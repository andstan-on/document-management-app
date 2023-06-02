package com.springframework.documentmanagementapp.services;

import com.springframework.documentmanagementapp.model.User;
import com.springframework.documentmanagementapp.model.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private Map<UUID, User> userMap;

    public UserServiceImpl(){
        this.userMap = new HashMap<>();

        User user1 = User.builder()
                .id(UUID.randomUUID())
                .username("username1")
                .password("123456")
                .firstName("fname1")
                .lastName("lname1")
                .email("email1")
                .dateOfBirth(LocalDate.now())
                .role(UserRole.REGULAR)
                .build();

        User user2 = User.builder()
                .id(UUID.randomUUID())
                .username("username2")
                .password("123456")
                .firstName("fname2")
                .lastName("lname2")
                .email("email2")
                .dateOfBirth(LocalDate.now())
                .role(UserRole.REGULAR)
                .build();

        User user3 = User.builder()
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
    public List<User> listUsers() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public Optional<User> getUserById(UUID id) {
        return Optional.ofNullable(userMap.get(id));
    }

    @Override
    public User saveNewUser(User user) {
        User savedUser = User.builder()
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
    public void updateUserById(UUID existingId, User user) {
        User existing = userMap.get(existingId);

        existing.setUsername(user.getUsername());
        existing.setPassword(user.getPassword());
        existing.setFirstName(user.getFirstName());
        existing.setLastName(user.getLastName());
        existing.setEmail(user.getEmail());
        existing.setDateOfBirth(user.getDateOfBirth());
        existing.setRole(user.getRole());

        userMap.put(existing.getId(), existing);

    }

    @Override
    public void deleteUserById(UUID userId) {
        userMap.remove(userId);
    }
}
