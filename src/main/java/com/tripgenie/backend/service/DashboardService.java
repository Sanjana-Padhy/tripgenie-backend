package com.tripgenie.backend.service;

import com.tripgenie.backend.dto.DashboardResponse;
import com.tripgenie.backend.dto.TripResponse;
import com.tripgenie.backend.entity.Trip;
import com.tripgenie.backend.entity.User;
import com.tripgenie.backend.repository.TripRepository;
import com.tripgenie.backend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private UserRepository userRepository;

    public DashboardResponse getDashboard(String email) {

        // Find logged-in user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Get all trips belonging to this user
        List<Trip> trips = tripRepository.findByUser(user);

        // Total trips
        long totalTrips = trips.size();

        // Current date
        LocalDate today = LocalDate.now();

        // Count upcoming trips
        long upcomingTrips = trips.stream()
                .filter(trip ->
                        trip.getStartDate() != null &&
                        LocalDate.parse(trip.getStartDate()).isAfter(today))
                .count();

        // Count completed trips
        long completedTrips = trips.stream()
                .filter(trip ->
                        trip.getEndDate() != null &&
                        LocalDate.parse(trip.getEndDate()).isBefore(today))
                .count();

        // Calculate total budget
        double totalBudget = trips.stream()
                .mapToDouble(Trip::getBudget)
                .sum();

        // Get latest 5 trips
        List<TripResponse> recentTrips = trips.stream()
                .sorted(Comparator.comparing(
                        Trip::getId,
                        Comparator.reverseOrder()))
                .limit(5)
                .map(trip -> new TripResponse(
                        trip.getId(),
                        trip.getDestination(),
                        trip.getStartDate(),
                        trip.getEndDate(),
                        trip.getBudget()
                ))
                .collect(Collectors.toList());

        return new DashboardResponse(
                totalTrips,
                upcomingTrips,
                completedTrips,
                totalBudget,
                recentTrips
        );
    }
}