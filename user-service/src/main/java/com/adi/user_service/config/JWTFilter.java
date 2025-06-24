package com.adi.user_service.config;

import com.adi.user_service.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;


@Component
public class JWTFilter {
    private final String SECRET = "thozhilsaayanaagarigamparpadhennavonaanpaavamallavooohoohoomugilmazhainannanaigiren";
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    // 1. Generate the JWT token
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getId())
                .claim("isAdmin", user.getIsAdmin())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*5))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    // 2. Extract the claims from the user
    public Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }
}
