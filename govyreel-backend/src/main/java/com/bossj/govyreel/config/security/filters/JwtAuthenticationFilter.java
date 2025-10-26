package com.bossj.govyreel.config.security.filters;

import java.io.IOException;
import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bossj.govyreel.config.security.JwtTokenProvider;
import com.bossj.govyreel.config.security.SecurityUser;
import com.bossj.govyreel.entities.User;
import com.bossj.govyreel.services.UserService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        log.debug("Processing request URI: {}", request.getRequestURI());
        try {
            String jwt = jwtTokenProvider.getJwtFromRequest(request);

            if (StringUtils.hasText(jwt)) {
                log.debug("JWT token found in request header");
                if (jwtTokenProvider.validateToken(jwt)) {
                    log.debug("JWT token is valid");
                    UUID userId = UUID.fromString(jwtTokenProvider.getUserIdFromToken(jwt));
                    log.debug("User ID from token: {}", userId);

                    User user = userService.findById(userId);
                    SecurityUser securityUser = (SecurityUser) userDetailsService.loadUserByUsername(user.getEmail());
                    log.debug("User details loaded for user ID: {}", userId);

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            securityUser, null, securityUser.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("User with ID '{}' authenticated successfully. Setting security context.", userId);
                } else {
                    log.warn("Invalid JWT token received for URI: {}", request.getRequestURI());
                }
            } else {
                log.trace("No JWT token found in request header for URI: {}", request.getRequestURI());
            }
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty: {}", ex.getMessage());
        } catch (Exception ex) {
            log.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
        log.trace("Finished processing request: {}", request.getRequestURI());
    }

}
