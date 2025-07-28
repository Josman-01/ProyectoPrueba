package com.josman.estudioJava.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.secret.key}")
    private String secret;

    @Value("${jwt.expiration.time}")
    private int expiration;

    public String generateToken(Authentication authentication) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        UserDetails mainUser = (UserDetails) authentication.getPrincipal();
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);
        List<String> roles = mainUser.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList());

        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", mainUser.getUsername());
        claims.put("roles", roles);
        claims.put("iat", now);
        claims.put("exp", expirationDate);
        return Jwts.builder()
                .claims(claims)
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public Claims extractAllClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        JwtParser parser = Jwts.parser().verifyWith(key).build();
        return parser.parseSignedClaims(token).getPayload();
    }
    
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }
}