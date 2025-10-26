package com.bossj.govyreel.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.bossj.govyreel.entities.RefreshToken;
import com.bossj.govyreel.entities.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    @Modifying()
    @Query("DELETE FROM RefreshToken rt WHERE rt.user = :user")
    Integer deleteByUser(User user);

    Optional<RefreshToken> findByUserIdAndToken(UUID userId, String token);

    Optional<RefreshToken> findByToken(String token);

    void deleteByToken(String token);

}
