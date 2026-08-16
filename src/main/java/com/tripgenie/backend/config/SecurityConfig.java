package com.tripgenie.backend.config;

import com.tripgenie.backend.security.JwtAuthenticationFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.security.web.AuthenticationEntryPoint;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http

                // =====================================================
                // CORS
                // =====================================================

                .cors(cors -> cors.configurationSource(
                        corsConfigurationSource()
                ))


                // =====================================================
                // CSRF
                // =====================================================

                .csrf(csrf -> csrf.disable())


                // =====================================================
                // STATELESS SESSION
                // =====================================================

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )


                // =====================================================
                // AUTHORIZATION
                // =====================================================

                .authorizeHttpRequests(auth -> auth

                        // CORS preflight
                        .requestMatchers(
                                org.springframework.http.HttpMethod.OPTIONS,
                                "/**"
                        ).permitAll()

                        // Authentication APIs
                        .requestMatchers(
                                "/api/auth/**"
                        ).permitAll()

                        // Test API
                        .requestMatchers(
                                "/api/test"
                        ).permitAll()

                        // AI API requires JWT
                        .requestMatchers(
                                "/api/ai/**"
                        ).authenticated()

                        // Everything else
                        .anyRequest().authenticated()
                )


                // =====================================================
                // JWT FILTER
                // =====================================================

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )


                // =====================================================
                // JWT AUTHENTICATION ENTRY POINT
                // Do NOT use HTTP Basic
                // =====================================================

                .exceptionHandling(exception ->

                        exception.authenticationEntryPoint(
                                (request, response, authException) -> {

                                    response.setStatus(
                                            HttpServletResponse.SC_UNAUTHORIZED
                                    );

                                    response.setContentType(
                                            "application/json"
                                    );

                                    response.getWriter().write(
                                            "{\"message\":\"Authentication required\"}"
                                    );
                                }
                        )
                );


        return http.build();
    }


    // ================================================================
    // CORS CONFIGURATION
    // ================================================================

    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of(
                        "http://localhost:5173"
                )
        );

        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS"
                )
        );

        configuration.setAllowedHeaders(
                List.of(
                        "*"
                )
        );

        configuration.setAllowCredentials(true);

        configuration.setMaxAge(3600L);


        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }


    // ================================================================
    // PASSWORD ENCODER
    // ================================================================

    @Bean
    PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }


    // ================================================================
    // AUTHENTICATION MANAGER
    // ================================================================

    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration config)
            throws Exception {

        return config.getAuthenticationManager();
    }
}