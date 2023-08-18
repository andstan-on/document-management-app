package com.springframework.documentmanagementapp.services;

import com.springframework.documentmanagementapp.entities.User;
import com.springframework.documentmanagementapp.mappers.UserMapper;
import com.springframework.documentmanagementapp.model.DocumentDTO;
import com.springframework.documentmanagementapp.model.UserDTO;
import com.springframework.documentmanagementapp.model.UserRole;
import com.springframework.documentmanagementapp.repositories.UserRepository;
import com.springframework.documentmanagementapp.webutils.WebUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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

    private final static int DEFAULT_PAGE = 0;
    private final static int DEFAULT_PAGE_SIZE = 10;

    //page request
    public PageRequest buildPageRequest(Integer pageNumber, Integer pageSize){
        int queryPageNumber;
        int queryPageSize;

        if(pageNumber != null && pageNumber > 0){
            queryPageNumber = pageNumber - 1;
        } else {
            queryPageNumber = DEFAULT_PAGE;
        }

        if(pageSize == null) {
            queryPageSize = DEFAULT_PAGE_SIZE;
        } else {
            if(pageSize > 100){
                queryPageSize = 100;
            } else {
                queryPageSize = pageSize;
            }
        }

        Sort sort = Sort.by(Sort.Order.asc("username"));

        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }

    @Override
    public List<UserDTO> listUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserDTO> pageUsers(String searchedCriteria, Integer pageNumber, Integer pageSize) {

        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        Page<User> userPage;

        if(!StringUtils.hasText(searchedCriteria)){
            userPage = userRepository.findAll(pageRequest);
        } else {
            userPage = userRepository.filterUsers(searchedCriteria, pageRequest);
        }


        return  userPage.map(userMapper::userToUserDto);
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
    public Optional<UserDTO> updateUserRole(UUID userId, UserRole userRole) {
        AtomicReference<Optional<UserDTO>> atomicReference = new AtomicReference<>();

        userRepository.findById(userId).ifPresentOrElse(foundUser -> {
            foundUser.setRole(userRole);
            atomicReference.set(Optional.of(userMapper.userToUserDto(userRepository.save(foundUser))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }


    @Override
    public Boolean deleteUserById(UUID userId) {
        if(userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
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
