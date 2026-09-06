package com.tripgenie.backend.service;

import com.tripgenie.backend.entity.SavedItinerary;
import com.tripgenie.backend.entity.Trip;
import com.tripgenie.backend.entity.User;
import com.tripgenie.backend.repository.SavedItineraryRepository;
import com.tripgenie.backend.repository.TripRepository;
import com.tripgenie.backend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.tripgenie.backend.dto.SavedItineraryResponse;

import java.util.stream.Collectors;

@Service
public class SavedItineraryService {

    @Autowired
    private SavedItineraryRepository savedItineraryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TripRepository tripRepository;

    @Transactional
    public SavedItinerary saveItinerary(
            Long tripId,
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() ->
                        new RuntimeException("Trip not found"));

        if (!trip.getUser().getId().equals(user.getId())) {
            throw new RuntimeException(
                    "You are not authorized to save this trip");
        }

        if (savedItineraryRepository
                .findByUserAndTrip(user, trip)
                .isPresent()) {

            throw new RuntimeException(
                    "Itinerary is already saved");
        }

        SavedItinerary savedItinerary =
                new SavedItinerary();

        savedItinerary.setUser(user);
        savedItinerary.setTrip(trip);

        trip.setSaved(true);

        tripRepository.save(trip);

        return savedItineraryRepository
                .save(savedItinerary);
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
                        trip.getBudget(),
                        trip.getTravelStyle(),
                        trip.getStatus(),
                        trip.isSaved()
                );
            })
            .collect(Collectors.toList());
}

    @Transactional
    public void removeSavedItinerary(
            Long tripId,
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() ->
                        new RuntimeException("Trip not found"));

        SavedItinerary savedItinerary =
                savedItineraryRepository
                        .findByUserAndTrip(user, trip)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Saved itinerary not found"));

        savedItineraryRepository.delete(savedItinerary);

        trip.setSaved(false);

        tripRepository.save(trip);
    }
}