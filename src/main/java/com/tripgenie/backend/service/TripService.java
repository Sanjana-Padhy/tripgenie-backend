
package com.tripgenie.backend.service;

import com.tripgenie.backend.dto.CreateTripRequest;
import com.tripgenie.backend.dto.GenerateItineraryRequest;
import com.tripgenie.backend.dto.TripResponse;
import com.tripgenie.backend.dto.UpdateTripRequest;
import com.tripgenie.backend.entity.Trip;
import com.tripgenie.backend.entity.User;
import com.tripgenie.backend.exception.ForbiddenOperationException;
import com.tripgenie.backend.exception.ResourceNotFoundException;
import com.tripgenie.backend.repository.TripRepository;
import com.tripgenie.backend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private UserRepository userRepository;

    // Create a regular trip
    public String createTrip(CreateTripRequest request, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Trip trip = new Trip();

        trip.setDestination(request.getDestination());
        trip.setStartDate(request.getStartDate());
        trip.setEndDate(request.getEndDate());
        trip.setBudget(request.getBudget());
        trip.setUser(user);

        tripRepository.save(trip);

        return "Trip Created Successfully";
    }

    // Create an AI-generated trip record
    public Long createAiTrip(
            GenerateItineraryRequest request,
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

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

    // Get trips belonging to the logged-in user
    public List<TripResponse> getUserTrips(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        List<Trip> trips = tripRepository.findByUser(user);

        return trips.stream()
                .map(trip -> new TripResponse(
                        trip.getId(),
                        trip.getDestination(),
                        trip.getStartDate(),
                        trip.getEndDate(),
                        trip.getBudget(),
                        trip.getTravelStyle(),
                        trip.getStatus()
                ))
                .collect(Collectors.toList());
    }

    // Delete a trip
    public String deleteTrip(Long tripId, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Trip not found"));

        if (!trip.getUser().getId().equals(user.getId())) {
            throw new ForbiddenOperationException(
                    "You cannot delete someone else's trip");
        }

        tripRepository.delete(trip);

        return "Trip Deleted Successfully";
    }

    // Update a trip
    public String updateTrip(
            Long tripId,
            UpdateTripRequest request,
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Trip not found"));

        if (!trip.getUser().getId().equals(user.getId())) {
            throw new ForbiddenOperationException("Unauthorized");
        }

        trip.setDestination(request.getDestination());
        trip.setStartDate(request.getStartDate());
        trip.setEndDate(request.getEndDate());
        trip.setBudget(request.getBudget());

        tripRepository.save(trip);

        return "Trip Updated Successfully";
    }
}
