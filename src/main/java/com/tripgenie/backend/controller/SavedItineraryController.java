package com.tripgenie.backend.controller;

import com.tripgenie.backend.dto.SavedItineraryResponse;
import com.tripgenie.backend.entity.SavedItinerary;
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
    public SavedItineraryResponse saveItinerary(
            @PathVariable Long tripId,
            Authentication authentication) {

        String email = authentication.getName();

        SavedItinerary savedItinerary =
                savedItineraryService
                        .saveItinerary(tripId, email);

        return new SavedItineraryResponse(
                savedItinerary.getId(),
                savedItinerary.getTrip().getId(),
                savedItinerary.getTrip().getDestination(),
                savedItinerary.getTrip().getBudget(),
                savedItinerary.getTrip().getTravelStyle(),
                savedItinerary.getTrip().getStatus(),
                savedItinerary.getTrip().isSaved()
        );
    }

    @GetMapping
    public List<SavedItineraryResponse> getSavedItineraries(
            Authentication authentication) {

        String email = authentication.getName();

        return savedItineraryService
                .getSavedItineraries(email);
    }

    @DeleteMapping("/{tripId}")
    public String removeSavedItinerary(
            @PathVariable Long tripId,
            Authentication authentication) {

        String email = authentication.getName();

        savedItineraryService
                .removeSavedItinerary(tripId, email);

        return "Itinerary removed from saved trips";
    }
}