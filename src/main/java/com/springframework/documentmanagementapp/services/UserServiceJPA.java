package com.springframework.documentmanagementapp.services;

import com.springframework.documentmanagementapp.entities.User;
import com.springframework.documentmanagementapp.mappers.UserMapper;
import com.springframework.documentmanagementapp.model.UserDTO;
import com.springframework.documentmanagementapp.repositories.UserRepository;
import com.springframework.documentmanagementapp.webutils.WebUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class UserServiceJPA implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDTO> listUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDTO> getUserById(UUID id) {
        return Optional.ofNullable(userMapper.userToUserDto(userRepository.findById(id)
                .orElse(null)));
    }

    @Override
    public UserDTO saveNewUser(UserDTO user) {
        return userMapper.userToUserDto(userRepository.save(userMapper.userDtoToUser(user)));
    }

    @Override
    public Optional<UserDTO> updateUserById(UUID existingId, UserDTO user) {
        AtomicReference<Optional<UserDTO>> atomicReference = new AtomicReference<>();

        userRepository.findById(existingId).ifPresentOrElse(foundUser -> {
            foundUser.setUsername(user.getUsername());

            foundUser.setFirstName(user.getFirstName());
            foundUser.setLastName(user.getLastName());
            foundUser.setEmail(user.getEmail());
            foundUser.setDateOfBirth(user.getDateOfBirth());
            foundUser.setRole(user.getRole());
            atomicReference.set(Optional.of(userMapper.userToUserDto(userRepository.save(foundUser))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    @Override
    public Boolean deleteUserById(UUID userId) {
        if(userRepository.existsById(userId)) {
            userRepository.findById(userId);
            return true;
        }
        return false;
    }

    @Override
    public Optional<UserDTO> getLoggedInUser() {

        User user = WebUtils.getLoggedInUser();

        return Optional.ofNullable(userMapper.userToUserDto(userRepository.findById(user.getId())
                .orElse(null)));
    }
}
