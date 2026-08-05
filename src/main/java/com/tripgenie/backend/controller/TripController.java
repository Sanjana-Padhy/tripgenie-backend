package com.tripgenie.backend.controller;

import com.tripgenie.backend.dto.CreateTripRequest;
import com.tripgenie.backend.dto.TripResponse;
import com.tripgenie.backend.service.TripService;
import com.tripgenie.backend.util.JwtUtil;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.tripgenie.backend.dto.UpdateTripRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    @Autowired
    private TripService tripService;

    @Autowired
    private JwtUtil jwtUtil;

    // Create Trip
    @PostMapping
    public String createTrip(
            @Valid @RequestBody CreateTripRequest request,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);

        String email = jwtUtil.extractUsername(token);

        return tripService.createTrip(request, email);
    }

    // Get All Trips
    @GetMapping
    public List<TripResponse> getTrips(

            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);

        String email = jwtUtil.extractUsername(token);

        return tripService.getUserTrips(email);
    }
    @DeleteMapping("/{id}")
    public String deleteTrip(
        @PathVariable Long id,
        Authentication authentication) {

        return tripService.deleteTrip(
            id,
            authentication.getName());
}
@PutMapping("/{id}")
public String updateTrip(
        @PathVariable Long id,
        @Valid @RequestBody UpdateTripRequest request,
        Authentication authentication) {

    return tripService.updateTrip(
            id,
            request,
            authentication.getName());
}
}