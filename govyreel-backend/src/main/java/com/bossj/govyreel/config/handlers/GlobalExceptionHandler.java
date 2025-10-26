package com.bossj.govyreel.config.handlers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bossj.govyreel.dtos.ApiResponse;
import com.bossj.govyreel.exceptions.AssetNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

        private final Environment environment;

        public GlobalExceptionHandler(Environment environment) {
                this.environment = environment;
        }

        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
                        MethodArgumentNotValidException ex) {
                Map<String, String> errors = new HashMap<>();
                ex.getBindingResult().getAllErrors().forEach((error) -> {
                        String fieldName = ((FieldError) error).getField();
                        String errorMessage = error.getDefaultMessage();
                        errors.put(fieldName, errorMessage);
                });
                log.error("Validation failed: {}", errors);

                ApiResponse<Map<String, String>> response = ApiResponse.<Map<String, String>>builder()
                                .success(false)
                                .message("Validation failed")
                                .data(errors)
                                .build();

                return ResponseEntity.badRequest().body(response);
        }

        @ResponseStatus(HttpStatus.FORBIDDEN)
        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<ApiResponse<Map<String, String>>> handleBadCredentialsExceptions(
                        BadCredentialsException ex) {
                Map<String, String> errors = new HashMap<>();

                log.error("Bad Credentials: {}", ex.getMessage());

                ApiResponse<Map<String, String>> response = ApiResponse.<Map<String, String>>builder()
                                .success(false)
                                .message(ex.getMessage())
                                .data(errors)
                                .build();

                boolean isSecureCookie = !Arrays.asList(environment.getActiveProfiles()).contains("dev");

                ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", "")
                                .httpOnly(true)
                                .path("/")
                                .maxAge(0)
                                .secure(isSecureCookie)
                                .sameSite(isSecureCookie ? "None" : "Lax")
                                .build();

                ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", "")
                                .httpOnly(true)
                                .path("/")
                                .maxAge(0)
                                .secure(isSecureCookie)
                                .sameSite(isSecureCookie ? "None" : "Lax")
                                .build();

                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                                .body(response);
        }

        @ResponseStatus(HttpStatus.NOT_FOUND)
        @ExceptionHandler(AssetNotFoundException.class)
        public ResponseEntity<ApiResponse<String>> handleAssetNotFoundExceptions(
                        AssetNotFoundException ex) {

                ApiResponse<String> response = ApiResponse.<String>builder()
                                .success(false)
                                .message(ex.getMessage())
                                .data(ex.getMessage())
                                .build();

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
}
