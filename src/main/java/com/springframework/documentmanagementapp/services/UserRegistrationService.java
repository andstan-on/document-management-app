package com.springframework.documentmanagementapp.services;

import com.springframework.documentmanagementapp.entities.Token;
import com.springframework.documentmanagementapp.entities.User;
import com.springframework.documentmanagementapp.exception.*;
import com.springframework.documentmanagementapp.mappers.UserMapper;
import com.springframework.documentmanagementapp.model.*;
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
    public static final String RESET_URL = "http://localhost:8080/api/v1/authentication/reset/password?token=%s";
    public static final String RESET_URL_VIEW = "http://localhost:8080/authentication/reset/password?token=%s";

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;


    @Transactional
    public void createUserInAdminPanel(CreateUserDTO createUserDTO){

        createUserDTO.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));
        User user = userMapper.userCreationDtotoUser(createUserDTO);

        boolean userEmailExists = userRepository.findByEmail(user.getEmail()).isPresent();
        if (userEmailExists){
            throw new EmailAlreadyExistsException("A user already exists with the same email");
        }

        boolean userUsernameExists = userRepository.findByUsername(user.getUsername()).isPresent();
        if (userUsernameExists){
            throw new UsernameAlreadyExistsException("A user already exists with the same username");
        }


        user.setLocked(false);
        User savedUser = userRepository.save(user);

    }

    @Transactional
    public String register(UserRegistrationDTO userDTO) {

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User user = userMapper.userRegistrationDtoToUser(userDTO);

        boolean userEmailExists = userRepository.findByEmail(user.getEmail()).isPresent();
        if (userEmailExists){
            throw new EmailAlreadyExistsException("A user already exists with the same email");
        }

        boolean userUsernameExists = userRepository.findByUsername(user.getUsername()).isPresent();
        if (userUsernameExists){
            throw new UsernameAlreadyExistsException("A user already exists with the same username");
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
            emailService.send(userDTO.getEmail(), userDTO.getUsername(),null,"confirm-email-v2" ,String.format(CONFIRMATION_URL, generatedToken));
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return generatedToken;
    }

    @Transactional
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
                        savedToken.getApp_user().getUsername(),
                        null,
                        "confirm-email-v2",
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

    public String resetPasswordLinkOnEmail(EmailDTO emailDTO,String url){

        //check if userEmail exists in db
        boolean userEmailExists = userRepository.findByEmail(emailDTO.getEmail()).isPresent();
        if (userEmailExists == false){
            throw new NoUserWithGivenEmailException("No user found with given email.");
        }

        User savedUser = userRepository.findByEmail(emailDTO.getEmail()).get();

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
            emailService.send(savedUser.getEmail(), savedUser.getUsername(),"Password reset","confirm-reset-password" ,String.format(url, generatedToken));
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return generatedToken;
    }

    public String ResetPassword(String token, ResetPasswordDTO passwordDTO) {
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
                        savedToken.getApp_user().getUsername(),
                        "Reset Password",
                        "confirm-reset-password",
                        String.format(RESET_URL, generatedToken)
                );
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return "Token expired, a new token has been sent to your email";
        }

        if(!passwordDTO.getNewPassword().equals(passwordDTO.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        User user = userRepository.findById(savedToken.getApp_user().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
        userRepository.save(user);

        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
        return "<h1>Password was succesfully reset</h1>";
    }

    public Boolean getPasswordResetForm(String token) {
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
                        savedToken.getApp_user().getUsername(),
                        "Reset Password",
                        "confirm-reset-password",
                        String.format(RESET_URL_VIEW, generatedToken)
                );
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }

    public boolean resetPasswordView(String token, ResetPasswordDTO resetPasswordDTO){
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
                        savedToken.getApp_user().getUsername(),
                        "Reset Password",
                        "confirm-reset-password",
                        String.format(RESET_URL_VIEW, generatedToken)
                );
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return false;
        }

        if(!resetPasswordDTO.getNewPassword().equals(resetPasswordDTO.getConfirmPassword())) {
            throw new PasswordsDoNotMatchException("Passwords do not match");
        }

        User user = userRepository.findById(savedToken.getApp_user().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setPassword(passwordEncoder.encode(resetPasswordDTO.getNewPassword()));
        userRepository.save(user);

        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
        return true;
    }
}
