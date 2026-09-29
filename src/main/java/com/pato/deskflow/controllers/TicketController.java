package com.pato.deskflow.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pato.deskflow.dto.request.TicketRequest;
import com.pato.deskflow.dto.response.TicketResponse;
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
  public ResponseEntity<?> save(@Valid @RequestBody TicketRequest ticketRequest){
    try{
      TicketResponse ticket = ticketService.save(ticketRequest);

      return ResponseEntity.status(HttpStatus.CREATED).body(ticket);
    }
    catch(RuntimeException e){
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("/assume/{idTicket}/{idAssume}")
  public ResponseEntity<?> assumeTicket(@PathVariable Long idTicket, @PathVariable Long idAssume){
    
    return ResponseEntity.ok(ticketService.assumeTicket(idTicket, idAssume));
  }




}
