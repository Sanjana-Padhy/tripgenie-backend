package com.tripgenie.backend.controller;

import com.tripgenie.backend.dto.DashboardResponse;
import com.tripgenie.backend.service.DashboardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public DashboardResponse getDashboard(
            Authentication authentication) {

        String email = authentication.getName();

        return dashboardService.getDashboard(email);
    }
}