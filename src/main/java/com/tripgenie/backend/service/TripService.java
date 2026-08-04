package com.tripgenie.backend.service;

import com.tripgenie.backend.dto.CreateTripRequest;
import com.tripgenie.backend.entity.Trip;
import com.tripgenie.backend.entity.User;
import com.tripgenie.backend.repository.TripRepository;
import com.tripgenie.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}