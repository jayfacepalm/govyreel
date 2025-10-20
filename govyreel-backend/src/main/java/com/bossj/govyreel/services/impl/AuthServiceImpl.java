package com.bossj.govyreel.services.impl;

import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.bossj.govyreel.config.security.JwtTokenProvider;
import com.bossj.govyreel.config.security.SecurityUser;
import com.bossj.govyreel.dtos.JwtResponse;
import com.bossj.govyreel.dtos.LoginRequest;
import com.bossj.govyreel.entities.RefreshToken;
import com.bossj.govyreel.entities.User;
import com.bossj.govyreel.services.AuthService;
import com.bossj.govyreel.services.RefreshTokenService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        log.info("Authenticating user ");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                    )
                );

        log.info("Authentication successful");
        String accessToken = tokenProvider.generateToken(authentication);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(authentication);

        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .userId(((SecurityUser) authentication.getPrincipal()).getUser().getId().toString())
                .build();
    }

    @Override
    public JwtResponse refreshToken(HttpServletRequest request) {
        log.info("Refreshing token");
        String accessToken = tokenProvider.getCookieFromRequest(request, "accessToken");
        UUID userId = UUID.fromString(tokenProvider.getUserIdFromToken(accessToken));

        log.info("Extracted user ID from access token: {}", userId);
        String refreshTokenVal = tokenProvider.getCookieFromRequest(request, "refreshToken");
        RefreshToken refreshToken = refreshTokenService.findByUserIdAndToken(userId, refreshTokenVal);
        User user = refreshToken.getUser();

        log.info("Authentication successful");
        String accessTokenNew = tokenProvider.generateTokenByUser(user);
        RefreshToken refreshTokenNew = refreshTokenService.createRefreshTokenByUser(user);

        return JwtResponse.builder()
                .accessToken(accessTokenNew)
                .refreshToken(refreshTokenNew.getToken())
                .userId(user.getId().toString())
                .build();
    }

    @Override
    public void logoutUser(String refreshToken) {
        refreshTokenService.removeTokenByToken(refreshToken);
    }
}
