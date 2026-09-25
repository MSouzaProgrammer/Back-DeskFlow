package com.pato.deskflow.dto;

import lombok.Builder;

@Builder
public record JWTUserData(Long userId, String email, String name) {
}
