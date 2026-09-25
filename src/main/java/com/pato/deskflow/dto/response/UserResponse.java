package com.pato.deskflow.dto.response;

import com.pato.deskflow.enuns.Access;
import com.pato.deskflow.enuns.Sector;

public record UserResponse(

        String name,

        Access access,

        Sector sector

) {
}