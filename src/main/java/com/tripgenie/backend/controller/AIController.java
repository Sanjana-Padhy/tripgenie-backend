package com.tripgenie.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripgenie.backend.dto.GenerateItineraryRequest;
import com.tripgenie.backend.entity.AiItinerary;
import com.tripgenie.backend.entity.Trip;
import com.tripgenie.backend.repository.AiItineraryRepository;
import com.tripgenie.backend.repository.TripRepository;
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

    @Autowired
    private AiItineraryRepository aiItineraryRepository;

    @Autowired
    private TripRepository tripRepository;


    @PostMapping("/generate")
    public String generateItinerary(
            @RequestBody GenerateItineraryRequest request,
            Authentication authentication) {

        System.out.println("AI Controller Hit");

        String email = authentication.getName();

        // 1. Generate itinerary using Gemini
        String itineraryJson =
                geminiService.generateItinerary(request);

        // 2. Create Trip record
        Long tripId =
                tripService.createAiTrip(request, email);

        try {

            // 3. Find the newly created Trip
            Trip trip = tripRepository.findById(tripId)
                    .orElseThrow(() ->
                            new RuntimeException("Trip not found"));

            // 4. Save complete Gemini itinerary
            AiItinerary aiItinerary =
                    new AiItinerary();

            aiItinerary.setTrip(trip);
            aiItinerary.setItineraryJson(itineraryJson);

            aiItineraryRepository.save(aiItinerary);

            // 5. Convert Gemini JSON to Map
            Map<String, Object> itinerary =
                    objectMapper.readValue(
                            itineraryJson,
                            new TypeReference<Map<String, Object>>() {}
                    );

            // 6. Add database Trip ID
            itinerary.put("tripId", tripId);

            // 7. Return JSON
            return objectMapper.writeValueAsString(itinerary);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to save AI itinerary",
                    e
            );
        }
    }


    @GetMapping("/trips/{tripId}")
    public String getAiItinerary(
            @PathVariable Long tripId,
            Authentication authentication) {

        String email = authentication.getName();

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() ->
                        new RuntimeException("Trip not found"));

        // Security: make sure this trip belongs to logged-in user
        if (!trip.getUser().getEmail().equals(email)) {
            throw new RuntimeException(
                    "You are not authorized to view this trip");
        }

        AiItinerary aiItinerary =
                aiItineraryRepository.findByTrip(trip)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "AI itinerary not found"));

        return aiItinerary.getItineraryJson();
    }
}