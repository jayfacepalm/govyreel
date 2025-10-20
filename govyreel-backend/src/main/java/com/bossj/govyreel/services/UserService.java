package com.bossj.govyreel.services;

import java.util.Optional;
import java.util.UUID;

import com.bossj.govyreel.dtos.LoginRequest;
import com.bossj.govyreel.entities.User;

public interface UserService {
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndPassword(LoginRequest loginRequest);

    User findById(UUID id);
}
