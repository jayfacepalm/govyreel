package com.bossj.govyreel.services.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.bossj.govyreel.config.security.SecurityUser;
import com.bossj.govyreel.entities.RefreshToken;
import com.bossj.govyreel.entities.User;
import com.bossj.govyreel.repositories.RefreshTokenRepository;
import com.bossj.govyreel.services.RefreshTokenService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public RefreshToken createRefreshToken(Authentication authentication) {
        User user = ((SecurityUser) authentication.getPrincipal()).getUser();
        return generateRefreshToken(user);
    }

    @Override
    @Transactional
    public RefreshToken createRefreshTokenByUser(User user) {
        return generateRefreshToken(user);
    }

    @Override
    public RefreshToken findByUserIdAndToken(UUID userId, String token) {
        return refreshTokenRepository.findByUserIdAndToken(userId, token)
                .orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));
    }

    private RefreshToken generateRefreshToken(User user) {
        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .expiryDate(LocalDateTime.now().plusDays(3))
                .token(UUID.randomUUID().toString())
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    @Transactional
    public void removeTokenByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    @Override
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));
    }

}
