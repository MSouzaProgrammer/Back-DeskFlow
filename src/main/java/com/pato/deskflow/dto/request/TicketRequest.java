package com.pato.deskflow.dto.request;

import com.pato.deskflow.enuns.Priority;
import com.pato.deskflow.enuns.Sector;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TicketRequest(
  @NotBlank (message = "Titulo vazio!")
  String title,

  @NotBlank(message = "Descrição vazia!")
  String description,

  @NotNull(message = "Selecione uma prioridade!")
  Priority priority,

  Long userIdRequest,
  Sector sectorApplicant,

  @NotNull(message = "Selecione o setor destinado!")
  Sector sectorDestiny
) {
}