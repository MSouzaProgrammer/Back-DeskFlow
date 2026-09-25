package com.pato.deskflow.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pato.deskflow.dto.request.TicketRequest;
import com.pato.deskflow.dto.response.TicketResponse;
import com.pato.deskflow.entidades.Ticket;
import com.pato.deskflow.entidades.User;
import com.pato.deskflow.enuns.TicketStatus;
import com.pato.deskflow.repository.TicketRepository;
import com.pato.deskflow.repository.UserRepository;

@Service
public class TicketService {

  private final TicketRepository ticketRepository;
  private final UserRepository userRepository;

  public TicketService(
      TicketRepository ticketRepository,
      UserRepository userRepository) {

    this.ticketRepository = ticketRepository;
    this.userRepository = userRepository;
  }

  public TicketResponse create(TicketRequest request) {

    Authentication authentication = SecurityContextHolder
        .getContext()
        .getAuthentication();

    if (authentication == null
        || !authentication.isAuthenticated()) {

      throw new RuntimeException(
          "User not authenticated");
    }

    String email = authentication.getName();

    User requester = userRepository
        .findByEmail(email)
        .orElseThrow(() -> new RuntimeException(
            "User not found: " + email));

    Ticket ticket = new Ticket();

    ticket.setTitle(request.title());
    ticket.setDescription(request.description());
    ticket.setPriority(request.priority());
    ticket.setSector(request.sector());
    ticket.setRequester(requester);
    ticket.setTicketStatus(TicketStatus.ABERTO);
    ticket.setOpenedAt(LocalDateTime.now());

    Ticket savedTicket = ticketRepository.save(ticket);

    return toResponse(savedTicket);
  }

  @Transactional(readOnly = true)
  public List<TicketResponse> getAll() {

    return ticketRepository
        .findAllByOrderByOpenedAtDesc()
        .stream()
        .map(this::toResponse)
        .toList();
  }

  @Transactional(readOnly = true)
  public TicketResponse getById(Long id) {

    Ticket ticket = ticketRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException(
            "Ticket not found: " + id));

    return toResponse(ticket);
  }

  @Transactional(readOnly = true)
  public List<TicketResponse> getMyTickets() {

    Authentication authentication = SecurityContextHolder
        .getContext()
        .getAuthentication();

    if (authentication == null
        || !authentication.isAuthenticated()) {

      throw new RuntimeException(
          "User not authenticated");
    }

    String email = authentication.getName();

    return ticketRepository
        .findByRequesterEmailOrderByOpenedAtDesc(email)
        .stream()
        .map(this::toResponse)
        .toList();
  }

  public TicketResponse assume(Long id) {

    Authentication authentication = SecurityContextHolder
        .getContext()
        .getAuthentication();

    if (authentication == null
        || !authentication.isAuthenticated()) {

      throw new RuntimeException(
          "User not authenticated");
    }

    String email = authentication.getName();

    User user = userRepository
        .findByEmail(email)
        .orElseThrow(() -> new RuntimeException(
            "User not found: " + email));

    Ticket ticket = ticketRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException(
            "Ticket not found: " + id));

    System.out.println("===== DEBUG ASSUME =====");
    System.out.println("USUARIO: " + user.getEmail());
    System.out.println("SETOR USUARIO: " + user.getSector());
    System.out.println("SETOR TICKET: " + ticket.getSector());
    System.out.println("TICKET ID: " + ticket.getId());
    System.out.println("========================");

    if (ticket.getSector() != user.getSector()) {
      throw new RuntimeException(
          "User does not belong to the ticket sector");
    }

    if (ticket.getAssignee() != null) {

      throw new RuntimeException(
          "Ticket already has an assignee");
    }

    ticket.setAssignee(user);
    ticket.setTicketStatus(TicketStatus.EM_ATENDIMENTO);

    Ticket savedTicket = ticketRepository.save(ticket);

    return toResponse(savedTicket);
  }

  private TicketResponse toResponse(Ticket ticket) {

    return new TicketResponse(
        ticket.getId(),
        ticket.getTitle(),
        ticket.getDescription(),
        ticket.getTicketStatus(),
        ticket.getPriority(),
        ticket.getRequester().getName(),
        ticket.getSector(),
        ticket.getAssignee() != null
            ? ticket.getAssignee().getName()
            : null,
        ticket.getOpenedAt(),
        ticket.getClosedAt());
  }
}