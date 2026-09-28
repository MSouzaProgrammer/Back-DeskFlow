package com.pato.deskflow.dto.response;

import com.pato.deskflow.enuns.Priority;
import com.pato.deskflow.enuns.Sector;
import com.pato.deskflow.enuns.TicketStatus;


public record TicketResponse(
  Long id,
  String title,
  String description,
  TicketStatus ticketStatus,
  Priority priority,
  String userNameRequester,
  Sector sectorApplicant,
  Sector sectorDestiny,
  String nameAssume
) {
}