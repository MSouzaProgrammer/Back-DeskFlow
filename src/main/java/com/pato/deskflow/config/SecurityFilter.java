package com.pato.deskflow.config;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pato.deskflow.dto.JWTUserData;
import com.pato.deskflow.entidades.User;
import com.pato.deskflow.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenConfig tokenConfig;
    private final UserRepository userRepository;

    public SecurityFilter(
            TokenConfig tokenConfig,
            UserRepository userRepository) {

        this.tokenConfig = tokenConfig;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader =
                request.getHeader("Authorization");

        if (authorizationHeader == null
                || !authorizationHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        String token =
                authorizationHeader.substring(7);

        Optional<JWTUserData> optionalUser =
                tokenConfig.validateToken(token);

        if (optionalUser.isEmpty()) {

            filterChain.doFilter(request, response);
            return;
        }

        JWTUserData userData =
                optionalUser.get();

        Optional<User> optionalUserEntity =
                userRepository.findByEmail(userData.email());

        if (optionalUserEntity.isEmpty()) {

            filterChain.doFilter(request, response);
            return;
        }

        User user =
                optionalUserEntity.get();

        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(
                        "ROLE_" + user.getAccess());

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        null,
                        Collections.singletonList(authority)
                );

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}