package com.pato.deskflow.config;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pato.deskflow.dto.JWTUserData;
import com.pato.deskflow.entidades.User;

@Component
public class TokenConfig {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user) {

        Algorithm algorithm =
                Algorithm.HMAC256(secret);

        return JWT.create()
                .withClaim("userId", user.getId())
                .withClaim("name", user.getName())
                .withSubject(user.getEmail())
                .withIssuedAt(Instant.now())
                .withExpiresAt(
                        Instant.now().plusSeconds(86400)
                )
                .sign(algorithm);
    }

    public Optional<JWTUserData> validateToken(
            String token) {

        try {

            Algorithm algorithm =
                    Algorithm.HMAC256(secret);

            DecodedJWT decodedJWT =
                    JWT.require(algorithm)
                            .build()
                            .verify(token);

            JWTUserData userData =
                    new JWTUserData(
                            decodedJWT
                                    .getClaim("userId")
                                    .asLong(),

                            decodedJWT.getSubject(),

                            decodedJWT
                                    .getClaim("name")
                                    .asString()
                    );

            return Optional.of(userData);

        } catch (JWTVerificationException ex) {

            return Optional.empty();
        }
    }
}