package com.springframework.documentmanagementapp.mappers;

import com.springframework.documentmanagementapp.entities.User;
import com.springframework.documentmanagementapp.model.UserDTO;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    User userDtoToUser(UserDTO dto);

    UserDTO userToUserDto(User user);
}
