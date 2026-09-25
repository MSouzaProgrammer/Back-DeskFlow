package com.pato.deskflow.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pato.deskflow.dto.request.TicketRequest;
import com.pato.deskflow.dto.response.TicketResponse;
import com.pato.deskflow.services.TicketService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<TicketResponse> create(
            @Valid @RequestBody TicketRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ticketService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<TicketResponse>> getAll() {

        return ResponseEntity.ok(
                ticketService.getAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ticketService.getById(id)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<List<TicketResponse>> getMyTickets() {

        return ResponseEntity.ok(
                ticketService.getMyTickets()
        );
    }

    @PostMapping("/{id}/assume")
    public ResponseEntity<TicketResponse> assume(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ticketService.assume(id)
        );
    }
}