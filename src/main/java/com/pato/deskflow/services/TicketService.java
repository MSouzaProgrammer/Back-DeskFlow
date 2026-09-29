package com.pato.deskflow.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

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

  public TicketService(TicketRepository ticketRepository, UserRepository userRepository) {
    this.ticketRepository = ticketRepository;
    this.userRepository = userRepository;
  }




  public TicketResponse save(TicketRequest ticketRequest){
    if(ticketRequest == null){
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

    User userIdRequest = userRepository.findById(ticketRequest.userIdRequest()).orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
    ticket.setUserRequester(userIdRequest);
    
    Ticket save =  ticketRepository.save(ticket);
    return new TicketResponse(
      save.getId(),
      save.getTitle(),
      save.getDescription(),
      save.getTicketStatus(),
      save.getPriority(),
      save.getUserRequester().getName(),
      save.getSectorApplicant(),
      save.getSectorDestiny(),
      save.getAssignee() != null ? save.getAssignee().getName() : null
    );
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
            save.getAssignee().getName()
    );
}
}
