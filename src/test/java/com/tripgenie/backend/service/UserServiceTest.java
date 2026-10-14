package com.tripgenie.backend.service;

import com.tripgenie.backend.dto.RegisterRequest;
import com.tripgenie.backend.entity.User;
import com.tripgenie.backend.exception.DuplicateResourceException;
import com.tripgenie.backend.repository.UserRepository;
import com.tripgenie.backend.util.JwtUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setFullName("Test User");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password123");
    }

    @Test
    void registerUser_ShouldSaveUser_WhenEmailDoesNotExist() {

        when(userRepository.existsByEmail("test@example.com"))
                .thenReturn(false);

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        String result = userService.registerUser(registerRequest);

        assertEquals("User Registered Successfully", result);

        verify(userRepository).existsByEmail("test@example.com");

        verify(userRepository).save(argThat(user ->
                user.getFullName().equals("Test User")
                && user.getEmail().equals("test@example.com")
                && user.getPassword() != null
                && !user.getPassword().equals("password123")
                && new BCryptPasswordEncoder()
                        .matches("password123", user.getPassword())
        ));
    }

    @Test
    void registerUser_ShouldThrowException_WhenEmailAlreadyExists() {

        when(userRepository.existsByEmail("test@example.com"))
                .thenReturn(true);

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> userService.registerUser(registerRequest)
        );

        assertEquals("Email already exists!", exception.getMessage());

        verify(userRepository).existsByEmail("test@example.com");

        verify(userRepository, never()).save(any(User.class));
    }
}