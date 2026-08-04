package com.tripgenie.backend.controller;

import com.tripgenie.backend.dto.CreateTripRequest;
import com.tripgenie.backend.service.TripService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    @Autowired
    private TripService tripService;

    @PostMapping
    public String createTrip(
            @Valid @RequestBody CreateTripRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        return tripService.createTrip(request, email);
    }
}