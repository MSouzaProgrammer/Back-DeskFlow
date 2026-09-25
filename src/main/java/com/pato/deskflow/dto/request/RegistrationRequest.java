package com.pato.deskflow.dto.request;

import com.pato.deskflow.enuns.Access;
import com.pato.deskflow.enuns.Sector;

import jakarta.validation.constraints.NotEmpty;

public record RegistrationRequest(

        @NotEmpty(message = "Name is required!")
        String name,

        @NotEmpty(message = "Email is required!")
        String email,

        @NotEmpty(message = "Password is required!")
        String password,

        Access access,

        Sector sector
) {
}