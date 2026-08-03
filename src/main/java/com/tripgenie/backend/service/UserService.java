package com.tripgenie.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tripgenie.backend.dto.LoginRequest;
import com.tripgenie.backend.dto.LoginResponse;
import com.tripgenie.backend.dto.RegisterRequest;
import com.tripgenie.backend.entity.User;
import com.tripgenie.backend.repository.UserRepository;
import com.tripgenie.backend.security.JwtUtil;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    // Register User
    public String registerUser(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            return "Email already exists!";
        }

        User user = new User();

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        return "User Registered Successfully";
    }

    // Login User
    public LoginResponse loginUser(LoginRequest request) {

        Optional<User> optionalUser =
                userRepository.findByEmail(request.getEmail());

        if (optionalUser.isEmpty()) {
            return new LoginResponse(null, "User not found");
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new LoginResponse(null, "Invalid Password");
        }

        String token = JwtUtil.generateToken(user.getEmail());

        return new LoginResponse(token, "Login Successful");
    }
}