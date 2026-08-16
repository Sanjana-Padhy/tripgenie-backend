package com.tripgenie.backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET =
            "TripGenieSecretKeyTripGenieSecretKey123456";


    private final SecretKey key =
            Keys.hmacShaKeyFor(
                    SECRET.getBytes(StandardCharsets.UTF_8)
            );


    // =========================================================
    // GENERATE TOKEN
    // =========================================================

    public String generateToken(String email) {

        return Jwts.builder()

                .subject(email)

                .issuedAt(new Date())

                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + 86400000
                        )
                )

                .signWith(key)

                .compact();
    }


    // =========================================================
    // EXTRACT USERNAME
    // =========================================================

    public String extractUsername(String token) {

        return extractAllClaims(token)
                .getSubject();
    }


    // =========================================================
    // VALIDATE TOKEN
    // =========================================================

    public boolean validateToken(
            String token,
            String email) {

        try {

            String username =
                    extractUsername(token);

            return username.equals(email)
                    && !isTokenExpired(token);

        } catch (Exception e) {

            System.out.println(
                    "JWT validation exception: "
                            + e.getMessage()
            );

            return false;
        }
    }


    // =========================================================
    // CHECK EXPIRY
    // =========================================================

    private boolean isTokenExpired(String token) {

        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }


    // =========================================================
    // READ CLAIMS
    // =========================================================

    private Claims extractAllClaims(String token) {

        return Jwts.parser()

                .verifyWith(key)

                .build()

                .parseSignedClaims(token)

                .getPayload();
    }
}