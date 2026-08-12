package com.tripgenie.backend.controller;

import com.tripgenie.backend.dto.SavedItineraryResponse;
import com.tripgenie.backend.service.SavedItineraryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/saved-itineraries")
public class SavedItineraryController {

    @Autowired
    private SavedItineraryService savedItineraryService;

    @PostMapping("/{tripId}")
    public String saveItinerary(
            @PathVariable Long tripId,
            Authentication authentication) {

        String email = authentication.getName();

        return savedItineraryService
                .saveItinerary(tripId, email);
    }

    @GetMapping
    public List<SavedItineraryResponse> getSavedItineraries(
            Authentication authentication) {

        String email = authentication.getName();

        return savedItineraryService
                .getSavedItineraries(email);
    }

    @DeleteMapping("/{savedId}")
    public String removeSavedItinerary(
            @PathVariable Long savedId,
            Authentication authentication) {

        String email = authentication.getName();

        return savedItineraryService
                .removeSavedItinerary(savedId, email);
    }
}