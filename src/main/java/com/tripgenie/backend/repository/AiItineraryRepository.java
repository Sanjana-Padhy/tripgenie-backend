package com.tripgenie.backend.repository;

import com.tripgenie.backend.entity.AiItinerary;
import com.tripgenie.backend.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AiItineraryRepository
        extends JpaRepository<AiItinerary, Long> {

    Optional<AiItinerary> findByTrip(Trip trip);
}