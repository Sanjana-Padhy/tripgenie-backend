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
}