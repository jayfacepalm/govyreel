package com.bossj.govyreel.config.security;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.bossj.govyreel.entities.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecretString;

    private SecretKey jwtSecret;

    private final long jwtExpirationInMs = 900000; // 15 minutes

    @PostConstruct
    public void init() {
        log.debug("Initializing JwtTokenProvider");
        this.jwtSecret = Keys.hmacShaKeyFor(jwtSecretString.getBytes(StandardCharsets.UTF_8));
        log.info("JwtTokenProvider initialized successfully");
    }

    public String getJwtFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    log.debug("accessToken found in cookie");
                    return cookie.getValue();
                }
            }
        }
        log.trace("accessToken not found in cookie for request URI: {}", request.getRequestURI());
        return null;
    }

    public String getCookieFromRequest(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public String generateToken(Authentication authentication) {
        SecurityUser userPrincipal = (SecurityUser) authentication.getPrincipal();
        return generateTokenByUser(userPrincipal.getUser());
    }

    public String generateTokenByUser(User user) {
        log.debug("Generating JWT for user: {}", user.getEmail());
        List<String> roles = new ArrayList<>(
                user.getRoleMappings().stream()
                        .map(rm -> rm.getRole().getName())
                        .toList());

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        String token = Jwts.builder()
                .subject(user.getId().toString())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(jwtSecret)
                .claim("displayName", user.getName())
                .claim("roles", roles.toArray())
                .compact();
        log.info("Successfully generated JWT for user: {}", user.getEmail());
        return token;
    }

    public String getUserIdFromToken(String token) {
        log.debug("Attempting to extract user ID from token");
        Claims claims = Jwts.parser()
                .verifyWith(jwtSecret)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String userId = claims.getSubject();
        log.debug("Successfully extracted user ID: {}", userId);
        return userId;
    }

    public boolean validateToken(String authToken) {
        log.debug("Validating JWT");
        try {
            Jwts.parser().verifyWith(jwtSecret).build().parseSignedClaims(authToken);
            log.info("JWT validation successful");
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        log.warn("JWT validation failed");
        return false;
    }
}
