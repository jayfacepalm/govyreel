package com.bossj.govyreel.controllers;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bossj.govyreel.dtos.ApiResponse;
import com.bossj.govyreel.dtos.JwtResponse;
import com.bossj.govyreel.dtos.LoginRequest;
import com.bossj.govyreel.services.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {
        private final AuthService authService;
        private final Environment environment;
        private final long refreshTokenDurationMs;

        public AuthController(AuthService authService, Environment environment,
                        @Value("${app.jwt.refreshTokenExpirationInMs}") long refreshTokenDurationMs) {
                this.authService = authService;
                this.environment = environment;
                this.refreshTokenDurationMs = refreshTokenDurationMs;
        }

        @PostMapping("/login")
        public ResponseEntity<ApiResponse<String>> login(@RequestBody @Valid LoginRequest request) {
                log.info("Login attempt");
                JwtResponse jwtResponse = authService.authenticateUser(request);
                return buildTokenResponse(jwtResponse, "Login Success");
        }

        @PostMapping("/refresh-token")
        public ResponseEntity<ApiResponse<String>> refreshToken(HttpServletRequest request) {
                log.info("Refresh token attempt");
                JwtResponse jwtResponse = authService.refreshToken(request);
                return buildTokenResponse(jwtResponse, "Token refreshed successfully");
        }

        @PostMapping("/logout")
        public ResponseEntity<ApiResponse<String>> logout(
                        @CookieValue("refreshToken") String refreshToken) {
                log.info("Logout attempt for refresh token: {}", refreshToken);
                if (!StringUtils.hasText(refreshToken)) {
                        authService.logoutUser(refreshToken);
                }

                return buildTokenResponse(
                                JwtResponse.builder().accessToken("").refreshToken("").build(),
                                "Logout successful");
        }

        private ResponseEntity<ApiResponse<String>> buildTokenResponse(JwtResponse jwtResponse, String successMessage) {

                boolean isSecureCookie = !Arrays.asList(environment.getActiveProfiles()).contains("dev");

                ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", jwtResponse.getAccessToken())
                                .httpOnly(true)
                                .path("/")
                                .maxAge(15 * 60) // 15 minutes
                                .secure(isSecureCookie)
                                .sameSite(isSecureCookie ? "None" : "Lax")
                                .build();

                ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", jwtResponse.getRefreshToken())
                                .httpOnly(true)
                                .path("/")
                                .maxAge(refreshTokenDurationMs / 1000)
                                .secure(isSecureCookie)
                                .sameSite(isSecureCookie ? "None" : "Lax")
                                .build();

                ApiResponse<String> response = ApiResponse.<String>builder()
                                .message(successMessage)
                                .build();

                log.info(successMessage);

                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                                .body(response);
        }
}