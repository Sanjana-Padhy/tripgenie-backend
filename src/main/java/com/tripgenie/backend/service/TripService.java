package com.tripgenie.backend.service;

import com.tripgenie.backend.dto.CreateTripRequest;
import com.tripgenie.backend.entity.Trip;
import com.tripgenie.backend.entity.User;
import com.tripgenie.backend.repository.TripRepository;
import com.tripgenie.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tripgenie.backend.dto.TripResponse;
import java.util.List;
import java.util.stream.Collectors;

import com.tripgenie.backend.dto.UpdateTripRequest;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private UserRepository userRepository;

    public String createTrip(CreateTripRequest request, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Trip trip = new Trip();

        trip.setDestination(request.getDestination());
        trip.setStartDate(request.getStartDate());
        trip.setEndDate(request.getEndDate());
        trip.setBudget(request.getBudget());

        trip.setUser(user);

        tripRepository.save(trip);

        return "Trip Created Successfully";
    }

    public Long createAiTrip(
        com.tripgenie.backend.dto.GenerateItineraryRequest request,
        String email) {

    User user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new RuntimeException("User not found"));

    Trip trip = new Trip();

    trip.setDestination(request.getDestination());

    trip.setBudget(request.getBudget());

    trip.setTravelStyle(request.getTravelStyle());

    trip.setStatus("AI_GENERATED");

    trip.setSaved(false);

    trip.setUser(user);

    Trip savedTrip = tripRepository.save(trip);

    return savedTrip.getId();
}

    public List<TripResponse> getUserTrips(String email) {

    User user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new RuntimeException("User not found"));

    List<Trip> trips = tripRepository.findByUser(user);

    return trips.stream()
            .map(trip -> new TripResponse(
                    trip.getId(),
                    trip.getDestination(),
                    trip.getStartDate(),
                    trip.getEndDate(),
                    trip.getBudget()
            ))
            .collect(Collectors.toList());
}
public String deleteTrip(Long tripId, String email) {

    User user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new RuntimeException("User not found"));

    Trip trip = tripRepository.findById(tripId)
            .orElseThrow(() ->
                    new RuntimeException("Trip not found"));

    if (!trip.getUser().getId().equals(user.getId())) {
        throw new RuntimeException("You cannot delete someone else's trip");
    }

    tripRepository.delete(trip);

    return "Trip Deleted Successfully";
}
public String updateTrip(Long tripId,
                         UpdateTripRequest request,
                         String email) {

    // Find logged-in user
    User user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new RuntimeException("User not found"));

    // Find trip
    Trip trip = tripRepository.findById(tripId)
            .orElseThrow(() ->
                    new RuntimeException("Trip not found"));

    // Check ownership
    if (!trip.getUser().getId().equals(user.getId())) {
        throw new RuntimeException("Unauthorized");
    }

    // Update fields
    trip.setDestination(request.getDestination());
    trip.setStartDate(request.getStartDate());
    trip.setEndDate(request.getEndDate());
    trip.setBudget(request.getBudget());

    // Save updated trip
    tripRepository.save(trip);

    return "Trip Updated Successfully";
}
}