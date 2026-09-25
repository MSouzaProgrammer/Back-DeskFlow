package com.pato.deskflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.pato.deskflow.repository.UserRepository;

@Configuration
public class SecurityConfig {

    private final SecurityFilter securityFilter;
    private final UserRepository userRepository;

    public SecurityConfig(
            SecurityFilter securityFilter,
            UserRepository userRepository) {

        this.securityFilter = securityFilter;
        this.userRepository = userRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {

        return username -> userRepository
                .findByEmail(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found: " + username));
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(provider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .authorizeHttpRequests(auth -> auth

                .requestMatchers(
                    "/auth/login",
                    "/auth/register",
                    "/auth/change-password"
                ).permitAll()

                .requestMatchers("/tickets/**").authenticated()

                .anyRequest().authenticated()
            )

            .addFilterBefore(
                securityFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}