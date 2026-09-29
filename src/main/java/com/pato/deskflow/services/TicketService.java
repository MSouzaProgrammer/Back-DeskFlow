package com.pato.deskflow.services;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

import com.pato.deskflow.dto.request.TicketRequest;
import com.pato.deskflow.dto.response.TicketResponse;
import com.pato.deskflow.entidades.Ticket;
import com.pato.deskflow.entidades.User;
import com.pato.deskflow.enuns.Sector;
import com.pato.deskflow.enuns.TicketStatus;
import com.pato.deskflow.repository.TicketRepository;
import com.pato.deskflow.repository.UserRepository;

@Service
public class TicketService {

  private final TicketRepository ticketRepository;
  private final UserRepository userRepository;

  public TicketService(TicketRepository ticketRepository, UserRepository userRepository) {
    this.ticketRepository = ticketRepository;
    this.userRepository = userRepository;
  }

  public TicketResponse save(TicketRequest ticketRequest) {
    if (ticketRequest == null) {
      return null;
    }
    Ticket ticket = new Ticket();
    ticket.setTitle(ticketRequest.title());
    ticket.setDescription(ticketRequest.description());
    ticket.setTicketStatus(TicketStatus.ABERTO);
    ticket.setPriority(ticketRequest.priority());
    ticket.setSectorApplicant(ticketRequest.sectorApplicant());
    ticket.setSectorDestiny(ticketRequest.sectorDestiny());
    ticket.setOpenedAt(LocalDateTime.now());

    User userIdRequest = userRepository.findById(ticketRequest.userIdRequest())
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
    ticket.setUserRequester(userIdRequest);

    Ticket save = ticketRepository.save(ticket);
    return new TicketResponse(
        save.getId(),
        save.getTitle(),
        save.getDescription(),
        save.getTicketStatus(),
        save.getPriority(),
        save.getUserRequester().getName(),
        save.getSectorApplicant(),
        save.getSectorDestiny(),
        save.getAssignee() != null ? save.getAssignee().getName() : null);
  }

  public TicketResponse assumeTicket(Long idTicket, Long idUAssume) {

    User assumer = userRepository.findById(idUAssume)
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    Ticket ticket = ticketRepository.findById(idTicket)
        .orElseThrow(() -> new RuntimeException("Ticket não encontrado"));

    ticket.setAssignee(assumer);

    Ticket save = ticketRepository.save(ticket);

    return new TicketResponse(
        save.getId(),
        save.getTitle(),
        save.getDescription(),
        save.getTicketStatus(),
        save.getPriority(),
        save.getUserRequester().getName(),
        save.getSectorApplicant(),
        save.getSectorDestiny(),
        save.getAssignee().getName());
  }

  public TicketResponse transfer(Long idTicket, Long idOldUser, Sector newSector){
    Ticket ticket = ticketRepository.findById(idTicket).orElseThrow(() -> new RuntimeException("ticket não encontrado!"));
    User oldUser = userRepository.findById(idOldUser).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    if(!ticket.getAssignee().getId().equals(oldUser.getId())){
      throw new RuntimeException("Esse usuário não assumiu esse chamado");
    }
    ticket.setAssignee(null);
    ticket.setSectorDestiny(newSector);
    ticketRepository.save(ticket);
    return new TicketResponse(
        ticket.getId(),
        ticket.getTitle(),
        ticket.getDescription(),
        ticket.getTicketStatus(),
        ticket.getPriority(),
        ticket.getUserRequester().getName(),
        ticket.getSectorApplicant(),
        ticket.getSectorDestiny(),
        ticket.getAssignee() != null ? ticket.getAssignee().getName() : null);
  }
}
