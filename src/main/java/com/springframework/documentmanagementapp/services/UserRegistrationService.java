package com.springframework.documentmanagementapp.services;

import com.springframework.documentmanagementapp.entities.Token;
import com.springframework.documentmanagementapp.entities.User;
import com.springframework.documentmanagementapp.mappers.UserMapper;
import com.springframework.documentmanagementapp.model.UserDTO;
import com.springframework.documentmanagementapp.model.UserRole;
import com.springframework.documentmanagementapp.repositories.TokenRepository;
import com.springframework.documentmanagementapp.repositories.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private static final String CONFIRMATION_URL = "http://localhost:8080/api/v1/authentication/confirm?token=%s";

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;

    @Transactional
    public String register(UserDTO userDTO) {

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User user = userMapper.userDtoToUser(userDTO);

        boolean userEmailExists = userRepository.findByEmail(user.getEmail()).isPresent();
        if (userEmailExists){
            throw new IllegalArgumentException("A user already exists with the same email");
        }

        boolean userUsernameExists = userRepository.findByUsername(user.getUsername()).isPresent();
        if (userUsernameExists){
            throw new IllegalArgumentException("A user already exists with the same username");
        }


        user.setRole(UserRole.REGULAR);
        User savedUser = userRepository.save(user);

        //Generate a token
        String generatedToken = UUID.randomUUID().toString();
        Token token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .app_user(savedUser)
                .build();

        tokenRepository.save(token);

        //send the confirmation email
        try {
            emailService.send(userDTO.getEmail(), userDTO.getFirstName(), null ,String.format(CONFIRMATION_URL, generatedToken));
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return generatedToken;
    }

    public String confirm(String token) {
        // get the token
        Token savedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalStateException("Token not found"));
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            // Generate a token
            String generatedToken = UUID.randomUUID().toString();
            Token newToken = Token.builder()
                    .token(generatedToken)
                    .createdAt(LocalDateTime.now())
                    .expiresAt(LocalDateTime.now().plusMinutes(10))
                    .app_user(savedToken.getApp_user())
                    .build();
            tokenRepository.save(newToken);
            try {
                emailService.send(
                        savedToken.getApp_user().getEmail(),
                        savedToken.getApp_user().getFirstName(),
                        null,
                        String.format(CONFIRMATION_URL, generatedToken)
                );
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return "Token expired, a new token has been sent to your email";
        }

        User user = userRepository.findById(savedToken.getApp_user().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);

        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
        return "<h1>Your account hase been successfully activated</h1>";
    }
}
