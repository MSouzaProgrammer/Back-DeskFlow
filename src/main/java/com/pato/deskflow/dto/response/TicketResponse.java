package com.pato.deskflow.dto.response;

import java.time.LocalDateTime;

import com.pato.deskflow.enuns.Priority;
import com.pato.deskflow.enuns.Sector;
import com.pato.deskflow.enuns.TicketStatus;

public record TicketResponse(

        Long id,

        String title,

        String description,

        TicketStatus status,

        Priority priority,

        String requester,

        Sector sector,

        String assignee,

        LocalDateTime openedAt,

        LocalDateTime closedAt

) {
}