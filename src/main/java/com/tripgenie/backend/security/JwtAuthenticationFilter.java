package com.tripgenie.backend.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import com.tripgenie.backend.service.CustomUserDetailsService;
import com.tripgenie.backend.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {


    @Autowired
    private JwtUtil jwtUtil;


    @Autowired
    private CustomUserDetailsService customUserDetailsService;


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {


        // =========================================================
        // CORS PRE-FLIGHT
        // =========================================================

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {

            filterChain.doFilter(request, response);

            return;
        }


        // =========================================================
        // REQUEST INFORMATION
        // =========================================================

        System.out.println();
        System.out.println("==========================================");
        System.out.println("JWT FILTER");
        System.out.println(
                request.getMethod()
                + " "
                + request.getRequestURI()
        );


        // =========================================================
        // GET AUTHORIZATION HEADER
        // =========================================================

        String authorizationHeader =
                request.getHeader("Authorization");


        System.out.println(
                "Authorization Header Present: "
                + (authorizationHeader != null)
        );


        String username = null;
        String jwtToken = null;


        // =========================================================
        // CHECK BEARER TOKEN
        // =========================================================

        if (authorizationHeader != null
                && authorizationHeader.startsWith("Bearer ")) {

            jwtToken =
                    authorizationHeader.substring(7);


            System.out.println(
                    "Bearer token received."
            );


            try {

                username =
                        jwtUtil.extractUsername(jwtToken);


                System.out.println(
                        "JWT username: "
                        + username
                );

            } catch (Exception e) {

                System.out.println(
                        "JWT extraction failed: "
                        + e.getMessage()
                );
            }
        }


        // =========================================================
        // AUTHENTICATE USER
        // =========================================================

        if (username != null
                && SecurityContextHolder
                    .getContext()
                    .getAuthentication() == null) {

            try {

                UserDetails userDetails =
                        customUserDetailsService
                                .loadUserByUsername(username);


                boolean validToken =
                        jwtUtil.validateToken(
                                jwtToken,
                                username
                        );


                System.out.println(
                        "JWT valid: "
                        + validToken
                );


                if (validToken) {

                    UsernamePasswordAuthenticationToken
                            authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );


                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );


                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(
                                    authentication
                            );


                    System.out.println(
                            "JWT authentication SUCCESSFUL"
                    );

                } else {

                    System.out.println(
                            "JWT authentication FAILED"
                    );
                }


            } catch (Exception e) {

                System.out.println(
                        "JWT authentication exception: "
                        + e.getMessage()
                );
            }
        }


        // =========================================================
        // CONTINUE REQUEST
        // =========================================================

        filterChain.doFilter(
                request,
                response
        );

        System.out.println(
                "=========================================="
        );
    }
}