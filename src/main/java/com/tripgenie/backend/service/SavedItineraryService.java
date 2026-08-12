package com.tripgenie.backend.service;

import com.tripgenie.backend.dto.SavedItineraryResponse;
import com.tripgenie.backend.entity.SavedItinerary;
import com.tripgenie.backend.entity.Trip;
import com.tripgenie.backend.entity.User;
import com.tripgenie.backend.repository.SavedItineraryRepository;
import com.tripgenie.backend.repository.TripRepository;
import com.tripgenie.backend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SavedItineraryService {

    @Autowired
    private SavedItineraryRepository savedItineraryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TripRepository tripRepository;

    public String saveItinerary(Long tripId, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() ->
                        new RuntimeException("Trip not found"));

        if (!trip.getUser().getId().equals(user.getId())) {
            throw new RuntimeException(
                    "You cannot save someone else's trip");
        }

        if (savedItineraryRepository
                .findByUserAndTrip(user, trip)
                .isPresent()) {

            return "Trip already saved";
        }

        SavedItinerary savedItinerary = new SavedItinerary();

        savedItinerary.setUser(user);
        savedItinerary.setTrip(trip);

        savedItineraryRepository.save(savedItinerary);

        return "Itinerary Saved Successfully";
    }

    public List<SavedItineraryResponse> getSavedItineraries(
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        List<SavedItinerary> savedItineraries =
                savedItineraryRepository.findByUser(user);

        return savedItineraries.stream()
                .map(saved -> {

                    Trip trip = saved.getTrip();

                    return new SavedItineraryResponse(
                            saved.getId(),
                            trip.getId(),
                            trip.getDestination(),
                            trip.getStartDate(),
                            trip.getEndDate(),
                            trip.getBudget()
                    );
                })
                .collect(Collectors.toList());
    }

    public String removeSavedItinerary(
            Long savedId,
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        SavedItinerary savedItinerary =
                savedItineraryRepository.findById(savedId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Saved itinerary not found"));

        if (!savedItinerary.getUser()
                .getId()
                .equals(user.getId())) {

            throw new RuntimeException(
                    "You cannot remove someone else's saved itinerary");
        }

        savedItineraryRepository.delete(savedItinerary);

        return "Saved Itinerary Removed Successfully";
    }
}