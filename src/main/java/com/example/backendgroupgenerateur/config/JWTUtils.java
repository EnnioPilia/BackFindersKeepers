package com.example.backendgroupgenerateur.config;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
@Component
public class JWTUtils {

    private final SecretKey key;
    private final int jwtExpirationMs;

public JWTUtils(@Value("${app.jwtSecret}") String jwtSecret,
                @Value("${app.jwtExpirationMs}") int jwtExpirationMs) {
    this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    this.jwtExpirationMs = jwtExpirationMs;
}

    public SecretKey getKey() {
    return key;
}


    // Génère un token avec juste le username
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // Génère un token avec email et rôle (forçant le préfixe "ROLE_")
    public String generateToken(String email, String role) {
        String prefixedRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        return Jwts.builder()
                .setSubject(email)
                .claim("role", prefixedRole)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return false;
    }


}
