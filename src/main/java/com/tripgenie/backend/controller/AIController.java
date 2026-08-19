package com.tripgenie.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripgenie.backend.dto.GenerateItineraryRequest;
import com.tripgenie.backend.service.GeminiService;
import com.tripgenie.backend.service.TripService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    @Autowired
    private GeminiService geminiService;

    @Autowired
    private TripService tripService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/generate")
    public String generateItinerary(
            @RequestBody GenerateItineraryRequest request,
            Authentication authentication) {

        System.out.println("AI Controller Hit");

        String email = authentication.getName();

        // Generate itinerary using Gemini
        String itineraryJson =
                geminiService.generateItinerary(request);

        // Create Trip record in database
        Long tripId =
                tripService.createAiTrip(request, email);

        try {

            // Convert Gemini JSON string into Map
            Map<String, Object> itinerary =
                    objectMapper.readValue(
                            itineraryJson,
                            new TypeReference<Map<String, Object>>() {}
                    );

            // Add database Trip ID
            itinerary.put("tripId", tripId);

            // Convert Map back into JSON
            return objectMapper.writeValueAsString(itinerary);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to add tripId to itinerary response",
                    e
            );
        }
    }
}