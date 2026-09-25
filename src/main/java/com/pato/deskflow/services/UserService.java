package com.pato.deskflow.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pato.deskflow.config.TokenConfig;
import com.pato.deskflow.dto.request.ChangePasswordRequest;
import com.pato.deskflow.dto.request.LoginRequest;
import com.pato.deskflow.dto.request.RegistrationRequest;
import com.pato.deskflow.dto.response.LoginResponse;
import com.pato.deskflow.dto.response.RegistrationResponse;
import com.pato.deskflow.entidades.User;
import com.pato.deskflow.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            TokenConfig tokenConfig) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenConfig = tokenConfig;
    }

    public LoginResponse login(LoginRequest request) {

        log.info(
                "Login attempt received for email: {}",
                request.email()
        );

        UsernamePasswordAuthenticationToken userAndPass =
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                );

        Authentication authentication =
                authenticationManager.authenticate(userAndPass);

        User user =
                (User) authentication.getPrincipal();

        String token =
                tokenConfig.generateToken(user);

        log.info(
                "User {} authenticated successfully! Access level: {} Sector: {}",
                user.getEmail(),
                user.getAccess(),
                user.getSector()
        );

        return new LoginResponse(
                token,
                user.getName(),
                user.getAccess()
        );
    }

    public RegistrationResponse register(
            RegistrationRequest request) {

        log.info(
                "Registration request received for email: {} with access: {} and sector: {}",
                request.email(),
                request.access(),
                request.sector()
        );

        User newUser = new User();

        newUser.setName(request.name());
        newUser.setEmail(request.email());
        newUser.setPassword(
                passwordEncoder.encode(request.password())
        );
        newUser.setAccess(request.access());
        newUser.setSector(request.sector());

        userRepository.save(newUser);

        return new RegistrationResponse(
                newUser.getName(),
                newUser.getEmail(),
                newUser.getAccess(),
                newUser.getSector()
        );
    }

    public ResponseEntity<String> changePassword(
            ChangePasswordRequest request) {

        var userDetails =
                userRepository.findByEmail(request.email());

        if (userDetails.isPresent()) {

            User user = userDetails.get();

            user.setPassword(
                    passwordEncoder.encode(
                            request.newPassword()
                    )
            );

            userRepository.save(user);

            return ResponseEntity.ok(
                    "Password updated successfully!"
            );
        }

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("User not found.");
    }
}