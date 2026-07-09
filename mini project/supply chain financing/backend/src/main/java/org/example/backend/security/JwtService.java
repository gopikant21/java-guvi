package org.example.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final SecretKey signingKey;
    private final long expirationMs;

    public JwtService(@Value("${security.jwt.secret}") String secret,
                      @Value("${security.jwt.expiration-ms}") long expirationMs) {
        this.signingKey = buildKey(secret);
        this.expirationMs = expirationMs;
    }

    public String generateToken(UserDetails userDetails, Map<String, Object> claims) {
        return Jwts.builder()
                .claims(new HashMap<>(claims))
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(signingKey)
                .compact();
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails, Map.of());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        Claims claims = Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimResolver.apply(claims);
    }

    private SecretKey buildKey(String rawSecret) {
        try {
            // Support both plain and base64 secret formats.
            byte[] keyBytes = rawSecret.matches("^[A-Za-z0-9+/=]+$")
                    ? Decoders.BASE64.decode(rawSecret)
                    : rawSecret.getBytes(StandardCharsets.UTF_8);
            if (keyBytes.length < 32) {
                keyBytes = rawSecret.getBytes(StandardCharsets.UTF_8);
            }
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception ignored) {
            return Keys.hmacShaKeyFor(rawSecret.getBytes(StandardCharsets.UTF_8));
        }
    }
}

