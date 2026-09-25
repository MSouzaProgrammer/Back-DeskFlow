package com.pato.deskflow.dto.request;

import java.time.LocalDateTime;

import com.pato.deskflow.entidades.Ticket;

public record MessageRequest(

        Ticket ticket,

        String content,

        LocalDateTime createdAt

) {
}