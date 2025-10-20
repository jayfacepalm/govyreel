package com.bossj.govyreel.services;

import com.bossj.govyreel.dtos.JwtResponse;
import com.bossj.govyreel.dtos.LoginRequest;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    JwtResponse authenticateUser(LoginRequest loginRequest);
    JwtResponse refreshToken(HttpServletRequest refreshToken);
    void logoutUser(String refreshToken);
}