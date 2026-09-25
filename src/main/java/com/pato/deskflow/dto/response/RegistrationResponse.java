package com.pato.deskflow.dto.response;

import com.pato.deskflow.enuns.Access;
import com.pato.deskflow.enuns.Sector;

public record RegistrationResponse(
        String name,
        String email,
        Access access,
        Sector sector
) {
}