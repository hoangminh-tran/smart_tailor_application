package com.smart.tailor.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

    String generateToken(UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);

    Claims extractAllClaims(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    String extractUsername(String token);

    Date extractExpirationDate(String token);

    boolean isValidToken(String token, UserDetails userDetails);

    boolean isTokenExpired(String token);

    Key getSignInKey();

    String extractUserIDFromJwtToken(String jwtToken);
}
