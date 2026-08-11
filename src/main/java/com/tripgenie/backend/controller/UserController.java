package com.tripgenie.backend.controller;

import com.tripgenie.backend.dto.UpdateProfileRequest;
import com.tripgenie.backend.dto.UserProfileResponse;
import com.tripgenie.backend.service.UserService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    // Get Logged-in User Profile
    @GetMapping("/api/user/profile")
    public UserProfileResponse getProfile(Authentication authentication) {

        return userService.getProfile(authentication.getName());
    }

    // Update Logged-in User Profile
    @PutMapping("/api/user/profile")
    public UserProfileResponse updateProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            Authentication authentication) {

        return userService.updateProfile(
                authentication.getName(),
                request);
    }
}