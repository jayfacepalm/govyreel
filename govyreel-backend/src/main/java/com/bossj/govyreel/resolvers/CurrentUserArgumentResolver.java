package com.bossj.govyreel.resolvers;

import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.bossj.govyreel.config.security.SecurityUser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(@NonNull MethodParameter parameter) {
        return parameter.getParameterAnnotation(CurrentUser.class) != null;
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
            @NonNull NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
        log.debug("Attempting to resolve @CurrentUser argument");
        // Get authentication details from Spring Security's context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            log.warn("Authentication is null, cannot resolve @CurrentUser");
            throw new BadCredentialsException("Invalid authentication");
        }

        if (!authentication.isAuthenticated()) {
            log.warn("User is not authenticated, cannot resolve @CurrentUser");
            throw new BadCredentialsException("Invalid authentication");
        }

        if (!(authentication.getPrincipal() instanceof UserDetails)) {
            log.warn("Principal is not an instance of UserDetails, cannot resolve @CurrentUser");
            throw new BadCredentialsException("Invalid authentication");
        }

        SecurityUser userDetails = (SecurityUser) authentication.getPrincipal();
        log.debug("Successfully resolved @CurrentUser for user ID: {}", userDetails.getUser().getId());
        return userDetails.getUser();
    }
}
