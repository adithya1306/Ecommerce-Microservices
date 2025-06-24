package com.adi.api_gateway;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Component
public class JwtFilterGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtFilterGatewayFilterFactory.Config> {

    @Autowired
    private JWTUtility jwtUtility;

    public JwtFilterGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (!request.getHeaders().containsKey("Authorization")) {
                return errorHandler(exchange, "Token missing", HttpStatus.UNAUTHORIZED);
            }

            String authHeader = request.getHeaders().getFirst("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return errorHandler(exchange, "Invalid Token", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);
            System.out.println("Auth Header: " + authHeader);
            System.out.println("Token: " + token);

            if (!jwtUtility.isValidToken(token)) {
                return errorHandler(exchange, "Token Expired or Invalid", HttpStatus.UNAUTHORIZED);
            }

            Claims claims = jwtUtility.extractClaims(token);
            String username = claims.getSubject();
            boolean role = claims.get("isAdmin", Boolean.class);

            System.out.println("Adding headers:");
            System.out.println("X-Username: " + username);
            System.out.println("X-Role: " + role);

            org.springframework.http.server.reactive.ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header("X-Username", username)
                    .header("X-Role", String.valueOf(role))
                    .build();

            return chain.filter(exchange.mutate().request((org.springframework.http.server.reactive.ServerHttpRequest) modifiedRequest).build());
        };
    }

    private Mono<Void> errorHandler(ServerWebExchange exchange, String errorMessage, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String errorJson = String.format("{\"error\": \"%s\", \"status\": %d}", errorMessage, status.value());

        DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
        DataBuffer dataBuffer = bufferFactory.wrap(errorJson.getBytes());

        return exchange.getResponse().writeWith(Mono.just(dataBuffer));
    }

    public static class Config {
        // config params if any
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.emptyList();
    }
}
