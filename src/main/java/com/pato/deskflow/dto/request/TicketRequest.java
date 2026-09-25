package com.pato.deskflow.dto.request;

import com.pato.deskflow.enuns.Priority;
import com.pato.deskflow.enuns.Sector;

public record TicketRequest(

  String title,
  String description,
  Priority priority,
  Sector sector
) {
}