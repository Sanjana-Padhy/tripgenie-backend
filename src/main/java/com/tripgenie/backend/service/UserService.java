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
import com.tripgenie.backend.util.JwtUtil;

import com.tripgenie.backend.dto.UserProfileResponse;

import com.tripgenie.backend.dto.UpdateProfileRequest;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

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

       String token = jwtUtil.generateToken(user.getEmail());
       
        return new LoginResponse(token, "Login Successful");
    }

   // Get Logged-in User Profile
public UserProfileResponse getProfile(String email) {

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    return new UserProfileResponse(
            user.getId(),
            user.getFullName(),
            user.getEmail()
    );
}

// Update Logged-in User Profile
public UserProfileResponse updateProfile(
        String currentEmail,
        UpdateProfileRequest request) {

    User user = userRepository.findByEmail(currentEmail)
            .orElseThrow(() -> new RuntimeException("User not found"));

    // Check whether the new email belongs to another user
    if (!currentEmail.equals(request.getEmail())
            && userRepository.existsByEmail(request.getEmail())) {

        throw new RuntimeException("Email already exists");
    }

    user.setFullName(request.getFullName());
    user.setEmail(request.getEmail());

    userRepository.save(user);

    return new UserProfileResponse(
            user.getId(),
            user.getFullName(),
            user.getEmail()
    );
}
}