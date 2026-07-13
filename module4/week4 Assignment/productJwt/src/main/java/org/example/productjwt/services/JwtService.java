package org.example.productjwt.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret.key:your-super-secret-key-that-is-at-least-32-characters-long!}")
    private String secretString;

    @Value("${jwt.expiration.time:3600000}")
    private long expirationTime;

    private static final String ROLES_CLAIM = "roles";

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username) {
        return generateToken(username, List.of());
    }

    public String generateToken(UserDetails userDetails) {
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return generateToken(userDetails.getUsername(), roles);
    }

    private String generateToken(String username, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(ROLES_CLAIM, roles);
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignKey())
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public List<String> extractRoles(String token) {
        Object rawRoles = extractAllClaims(token).get(ROLES_CLAIM);
        if (rawRoles instanceof Collection<?> roleCollection) {
            return roleCollection.stream().map(String::valueOf).toList();
        }
        return List.of();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean validateToken(String token, String username) {
        final String tokenUsername = extractUsername(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}



