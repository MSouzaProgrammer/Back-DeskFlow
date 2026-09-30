package com.pato.deskflow.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pato.deskflow.dto.request.TicketRequest;
import com.pato.deskflow.dto.response.HistoryResponse;
import com.pato.deskflow.dto.response.TicketResponse;
import com.pato.deskflow.enuns.Priority;
import com.pato.deskflow.enuns.Sector;
import com.pato.deskflow.services.TicketService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/ticket")
public class TicketController {

  private final TicketService ticketService;

  public TicketController(TicketService ticketService) {
    this.ticketService = ticketService;
  }

  @PostMapping("/create")
  public ResponseEntity<?> save(@Valid @RequestBody TicketRequest ticketRequest) {
    try {
      TicketResponse ticket = ticketService.save(ticketRequest);

      return ResponseEntity.status(HttpStatus.CREATED).body(ticket);
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PutMapping("/assume/{idTicket}/{idAssume}")
  public ResponseEntity<TicketResponse> assumeTicket(@PathVariable Long idTicket, @PathVariable Long idAssume) {

    return ResponseEntity.ok(ticketService.assumeTicket(idTicket, idAssume));
  }

  @PutMapping("/transfer/{idTicket}/{idOldUser}/{newSector}")
  public ResponseEntity<TicketResponse> transfer(@PathVariable Long idTicket, @PathVariable Long idOldUser,
      @PathVariable Integer newSector) {
    Sector sector = Sector.values()[newSector];
    return ResponseEntity.ok(ticketService.transfer(idTicket, idOldUser, sector));
  }

  @PutMapping("/close/{idTicket}/{idUser}")
  public ResponseEntity<TicketResponse> closer(@PathVariable Long idTicket, @PathVariable Long idUser) {
    return ResponseEntity.ok(ticketService.closeTicket(idTicket, idUser));
  }

  @PutMapping("/Updatepriority/{idTicket}/{idUser}/{newPriority}")
  public ResponseEntity<TicketResponse> updateTicketPriority(@PathVariable Long idTicket, @PathVariable Long idUser,
      @PathVariable Integer newPriority) {
    Priority priority = Priority.values()[newPriority];
    return ResponseEntity.ok(ticketService.updateTicketPriority(idTicket, idUser, priority));
  }

  @GetMapping
  public ResponseEntity<List<TicketResponse>> findAllTicket() {
    return ResponseEntity.ok(ticketService.findAllTicket());
  }

  @GetMapping("/{idTicket}")
  public ResponseEntity<TicketResponse> findByIdTicket(@PathVariable Long idTicket) {
    return ResponseEntity.ok(ticketService.findByIdTicket(idTicket));
  }

  @GetMapping("/history/{idTicket}")
  public ResponseEntity<List<HistoryResponse>> history(
      @PathVariable Long idTicket) {

    return ResponseEntity.ok(
        ticketService.getHistory(idTicket));
  }
}
