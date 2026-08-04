package com.tripgenie.backend.repository;

import com.tripgenie.backend.entity.Trip;
import com.tripgenie.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findByUser(User user);

}