package com.aegisops.auth.controller;

import com.aegisops.auth.security.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtTokenProvider tokenProvider;

    public AuthController(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @GetMapping("/mock-token")
    public ResponseEntity<Map<String, String>> getMockToken(@RequestParam(value = "role", defaultValue = "DEV") String role) {
        String userId = "user-" + UUID.randomUUID().toString().substring(0, 8);
        List<String> roles = Arrays.stream(role.split(","))
                .map(String::trim)
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        String token = tokenProvider.generateToken(userId, roles);
        return ResponseEntity.ok(Map.of("token", token));
    }
}
