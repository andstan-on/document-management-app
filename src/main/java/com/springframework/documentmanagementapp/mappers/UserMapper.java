package com.springframework.documentmanagementapp.mappers;

import com.springframework.documentmanagementapp.entities.User;
import com.springframework.documentmanagementapp.model.CreateUserDTO;
import com.springframework.documentmanagementapp.model.UserDTO;
import com.springframework.documentmanagementapp.model.UserRegistrationDTO;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    User userDtoToUser(UserDTO dto);

    UserDTO userToUserDto(User user);

    User userRegistrationDtoToUser(UserRegistrationDTO registrationDTO);

    User userCreationDtotoUser(CreateUserDTO createUserDTO);
}
