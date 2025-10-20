package com.bossj.govyreel.services.impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bossj.govyreel.dtos.LoginRequest;
import com.bossj.govyreel.entities.User;
import com.bossj.govyreel.repositories.UserRepository;
import com.bossj.govyreel.services.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByEmailAndPassword(LoginRequest loginRequest) {
        Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());

        if (user.isEmpty()) {
            return Optional.empty();
        }

        User foundUser = user.get();

        if (passwordEncoder.matches(loginRequest.getPassword(), foundUser.getPassword())) {
            return user;
        }
        return Optional.empty();
    }

    @Override
    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));
    }

}
