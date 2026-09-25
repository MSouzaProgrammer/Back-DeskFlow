package com.pato.deskflow.dto.response;

import com.pato.deskflow.enuns.Access;

public record LoginResponse(

        String token,

        String name,

        Access access

) {
}