package com.bossj.govyreel.config.security;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bossj.govyreel.entities.User;
import com.bossj.govyreel.services.UserService;

@Service
public class SecurityUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public SecurityUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        Set<GrantedAuthority> authorities = user.getRoleMappings().stream()
                .map(roleMapping -> new SimpleGrantedAuthority(roleMapping.getRole().getName()))
                .collect(Collectors.toSet());

        return new SecurityUser(user, user.getEmail(), user.getPassword(), authorities);
    }
}
