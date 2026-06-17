package com.aegisops.auth.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    @Value("${aegisops.jwt.secret:mock-jwt-secret-key-12345678901234567890123456789012}")
    private String jwtSecret;

    @Value("${aegisops.jwt.expiration-ms:86400000}") // 24 hours
    private long jwtExpirationMs;

    public String generateToken(String userId, List<String> roles) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .subject(userId)
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }
}
