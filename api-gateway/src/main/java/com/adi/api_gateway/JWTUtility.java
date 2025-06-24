package com.adi.api_gateway;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JWTUtility {

    private final String SECRET = "thozhilsaayanaagarigamparpadhennavonaanpaavamallavooohoohoomugilmazhainannanaigiren";

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isValidToken(String token) {
        try {
            Claims claims = extractClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false; // token is malformed, tampered, or invalid
        }
    }
}
