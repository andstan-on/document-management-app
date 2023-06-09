package com.springframework.documentmanagementapp.controller;

import com.springframework.documentmanagementapp.model.UserDTO;
import com.springframework.documentmanagementapp.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {
    public static final String USER_PATH = "/api/v1/user";
    public static final String USER_PATH_ID = USER_PATH + "/{userId}";

    private final UserService userService;

    @DeleteMapping(USER_PATH_ID)
    public ResponseEntity deleteById(@PathVariable("userId") UUID userId){
        if(! userService.deleteUserById(userId)) {
            throw new NotFoundException();
        }

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping(USER_PATH_ID)
    public ResponseEntity updateById(@PathVariable("userId") UUID existingId, @Validated @RequestBody UserDTO user){
        if (userService.updateUserById(existingId, user).isEmpty())
            throw new NotFoundException();


        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(USER_PATH)
    public ResponseEntity handlePost(@Validated @RequestBody UserDTO user){
        UserDTO savedUser = userService.saveNewUser(user);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", USER_PATH + "/" + savedUser.getId().toString());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping(USER_PATH)
    public List<UserDTO> listUsers(){
        return userService.listUsers();
    }

    @GetMapping(USER_PATH_ID)
    public UserDTO getUserById(@PathVariable("userId") UUID userId) {

        return userService.getUserById(userId).orElseThrow(NotFoundException::new);
    }

}
