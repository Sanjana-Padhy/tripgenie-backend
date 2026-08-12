package com.tripgenie.backend.repository;

import com.tripgenie.backend.entity.SavedItinerary;
import com.tripgenie.backend.entity.Trip;
import com.tripgenie.backend.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SavedItineraryRepository
        extends JpaRepository<SavedItinerary, Long> {

    List<SavedItinerary> findByUser(User user);

    Optional<SavedItinerary> findByUserAndTrip(User user, Trip trip);
}