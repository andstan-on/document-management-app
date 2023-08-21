package com.springframework.documentmanagementapp.services;

import com.springframework.documentmanagementapp.entities.Token;
import com.springframework.documentmanagementapp.model.DocumentDTO;

import java.util.List;
import java.util.UUID;

public interface TokenService {

    void deleteExpiredTokens();

    List<Token> listTokens(UUID userId);

    Boolean deleteById(Integer userId);

    void deleteUsersTokens(UUID userId);

}
