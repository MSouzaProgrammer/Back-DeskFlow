package com.pato.deskflow.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record ChangePasswordRequest(

        @NotEmpty(message = "Email is required!")
        String email,

        @NotEmpty(message = "New password is required!")
        String newPassword

) {
}