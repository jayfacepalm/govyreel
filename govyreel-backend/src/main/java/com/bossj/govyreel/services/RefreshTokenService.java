package com.bossj.govyreel.services;

import java.util.UUID;

import org.springframework.security.core.Authentication;

import com.bossj.govyreel.entities.RefreshToken;
import com.bossj.govyreel.entities.User;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(Authentication authentication);
    RefreshToken createRefreshTokenByUser(User user);
    RefreshToken findByUserIdAndToken(UUID userId, String token);
    RefreshToken findByToken(String token);
    void removeTokenByToken(String token);
}
