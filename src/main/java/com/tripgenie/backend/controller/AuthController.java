package com.tripgenie.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.tripgenie.backend.dto.LoginRequest;
import com.tripgenie.backend.dto.LoginResponse;
import com.tripgenie.backend.dto.RegisterRequest;
import com.tripgenie.backend.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // Register
    @PostMapping("/register")
    public String register(
        @Valid
        @RequestBody RegisterRequest request) {

    return userService.registerUser(request);
}

    // Login
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return userService.loginUser(request);
    }
}