package com.springframework.documentmanagementapp.services;

import com.springframework.documentmanagementapp.entities.Document;
import com.springframework.documentmanagementapp.entities.Token;
import com.springframework.documentmanagementapp.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Primary
public class TokenServiceJpa implements TokenService{

    private final TokenRepository tokenRepository;

    @Autowired
    public TokenServiceJpa(final TokenRepository tokenRepository){
        this.tokenRepository=tokenRepository;
    }

    @Transactional
    @Override
    public void deleteExpiredTokens() {
        List<Token> tokens = tokenRepository.findAll();
        for(Token token : tokens) {
            if (LocalDateTime.now().isAfter(token.getExpiresAt())) {
                tokenRepository.delete(token);
            }
        }
    }

    @Transactional
    @Override
    public List<Token> listTokens(UUID userId) {

        List<Token> tokenList;

        if(userId == null) {
            tokenList = tokenRepository.findAll();
        } else {
            tokenList = tokenRepository.findByAppUserId(userId);
        }

        return tokenList;

    }

    @Transactional
    @Override
    public Boolean deleteById(Integer userId) {
        if(tokenRepository.existsById(userId)) {
            tokenRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public void deleteUsersTokens(UUID userId) {
        tokenRepository.deleteAllByAppUserId(userId);
    }
}
