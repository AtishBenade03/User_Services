package com.UserService.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component  // Marks this class as a Spring component to be automatically detected and registered
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")  // Injects JWT secret key from application properties
    private String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds}")  // Injects JWT expiration time from application properties
    private int jwtExpirationInMs;

    // Generate JWT token for a given user
    public String generateToken(UserDetails user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getAuthorities());  // Adding user roles (authorities) to claims

        return Jwts.builder()
                .setClaims(claims)  // Set custom claims
                .setSubject(user.getUsername())  // Set subject (username)
                .setIssuedAt(new Date(System.currentTimeMillis()))  // Set token issue date
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))  // Set token expiration date
                .signWith(SignatureAlgorithm.HS512, jwtSecret)  // Sign the JWT with the secret key using HS512 algorithm
                .compact();  // Build the JWT and serialize it to a compact, URL-safe string
    }

    // Extract username from JWT token
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)  // Parse and validate the token
                .getBody();  // Get the body of the token, which contains claims

        return claims.getSubject();  // Retrieve subject (username) from claims
    }

    // Validate JWT token
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);  // Parse and validate the token using the secret key
            return true;  // Token is valid
        } catch (ExpiredJwtException ex) {
            throw new RuntimeException("Expired JWT token");  // Token has expired
        } catch (Exception ex) {
            throw new RuntimeException("Invalid JWT token");  // Other validation errors
        }
    }
}
