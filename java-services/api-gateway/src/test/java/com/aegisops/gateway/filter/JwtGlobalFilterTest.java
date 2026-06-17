package com.aegisops.gateway.filter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class JwtGlobalFilterTest {

    private JwtGlobalFilter filter;
    private final String secret = "mock-jwt-secret-key-12345678901234567890123456789012";

    @BeforeEach
    public void setup() {
        filter = new JwtGlobalFilter();
        ReflectionTestUtils.setField(filter, "jwtSecret", secret);
    }

    @Test
    public void testSkipPublicRoutes() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/v1/auth/mock-token").build()
        );

        GatewayFilterChain chain = Mockito.mock(GatewayFilterChain.class);
        when(chain.filter(any())).thenReturn(Mono.empty());

        Mono<Void> result = filter.filter(exchange, chain);

        StepVerifier.create(result).verifyComplete();
    }

    @Test
    public void testMissingAuthorizationHeader() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/v1/workflows").build()
        );

        GatewayFilterChain chain = Mockito.mock(GatewayFilterChain.class);

        Mono<Void> result = filter.filter(exchange, chain);

        StepVerifier.create(result).verifyComplete();
        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
    }

    @Test
    public void testValidTokenInjectedHeaders() {
        String token = Jwts.builder()
                .subject("user-999")
                .claim("roles", List.of("SRE"))
                .expiration(new Date(System.currentTimeMillis() + 60000))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .compact();

        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/v1/workflows")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .build()
        );

        GatewayFilterChain chain = exchangeMutated -> {
            String userId = exchangeMutated.getRequest().getHeaders().getFirst("X-User-Id");
            String roles = exchangeMutated.getRequest().getHeaders().getFirst("X-User-Roles");
            assertEquals("user-999", userId);
            assertEquals("SRE", roles);
            return Mono.empty();
        };

        Mono<Void> result = filter.filter(exchange, chain);

        StepVerifier.create(result).verifyComplete();
    }

    @Test
    public void testInvalidTokenReturnsUnauthorized() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/v1/workflows")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer invalid-token-value")
                        .build()
        );

        GatewayFilterChain chain = Mockito.mock(GatewayFilterChain.class);

        Mono<Void> result = filter.filter(exchange, chain);

        StepVerifier.create(result).verifyComplete();
        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
    }
}
