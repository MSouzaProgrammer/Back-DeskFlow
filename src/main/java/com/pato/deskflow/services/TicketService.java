package com.pato.deskflow.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pato.deskflow.dto.request.TicketRequest;
import com.pato.deskflow.dto.response.HistoryResponse;
import com.pato.deskflow.dto.response.TicketResponse;
import com.pato.deskflow.entidades.History;
import com.pato.deskflow.entidades.Ticket;
import com.pato.deskflow.entidades.User;
import com.pato.deskflow.enuns.Priority;
import com.pato.deskflow.enuns.Sector;
import com.pato.deskflow.enuns.TicketStatus;
import com.pato.deskflow.repository.HistoryRepository;
import com.pato.deskflow.repository.TicketRepository;
import com.pato.deskflow.repository.UserRepository;

@Service
public class TicketService {

  private final TicketRepository ticketRepository;
  private final UserRepository userRepository;
  private final HistoryRepository historyRepository;

  public TicketService(TicketRepository ticketRepository, UserRepository userRepository,
      HistoryRepository historyRepository) {
    this.ticketRepository = ticketRepository;
    this.userRepository = userRepository;
    this.historyRepository = historyRepository;
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
    History history = new History(
        null,
        ticket,
        userIdRequest,
        "Chamado criado",
        LocalDateTime.now());

    historyRepository.save(history);
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

    if (!ticket.getTicketStatus().equals(TicketStatus.ABERTO)) {
      throw new RuntimeException("Ticket já fechado");
    }

    ticket.setAssignee(assumer);
    ticket.setTicketStatus(TicketStatus.EM_ATENDIMENTO);
    Ticket save = ticketRepository.save(ticket);

    History history = new History(
        null,
        ticket,
        assumer,
        "Chamado assumido",
        LocalDateTime.now());

    historyRepository.save(history);
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

  public TicketResponse transfer(Long idTicket, Long idOldUser, Sector newSector) {
    Ticket ticket = ticketRepository.findById(idTicket)
        .orElseThrow(() -> new RuntimeException("ticket não encontrado!"));
    User oldUser = userRepository.findById(idOldUser).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    if (ticket.getAssignee() == null) {
      throw new RuntimeException("Ticket não possui responsável");
    }
    if (!ticket.getAssignee().getId().equals(oldUser.getId())) {
      throw new RuntimeException("Esse usuário não assumiu esse chamado");
    } else if (!ticket.getTicketStatus().equals(TicketStatus.EM_ATENDIMENTO)) {
      throw new RuntimeException("Ticket já fechado");
    }

    ticket.setAssignee(null);
    ticket.setSectorDestiny(newSector);
    ticket.setTicketStatus(TicketStatus.TRANSFERIDO);
    ticketRepository.save(ticket);

    History history = new History(
        null,
        ticket,
        oldUser,
        "Chamado transferido para o setor " + newSector,
        LocalDateTime.now());

    historyRepository.save(history);
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

  public TicketResponse closeTicket(Long idTicket, Long idUser) {
    Ticket ticket = ticketRepository.findById(idTicket)
        .orElseThrow(() -> new RuntimeException("ticket não encontrado!"));
    User user = userRepository.findById(idUser).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    if (!ticket.getAssignee().getId().equals(user.getId())) {
      throw new RuntimeException("Ticket não assumido pelo usuário");
    }
    ticket.setClosedAt(LocalDateTime.now());
    ticket.setTicketStatus(TicketStatus.RESOLVIDO);

    History history = new History(
        null,
        ticket,
        user,
        "Chamado fechado",
        LocalDateTime.now());

    historyRepository.save(history);
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

  public TicketResponse updateTicketPriority(
      Long idTicket,
      Long idUser,
      Priority priority) {

    Ticket ticket = ticketRepository.findById(idTicket)
        .orElseThrow(() -> new RuntimeException("Ticket não encontrado!"));

    User user = userRepository.findById(idUser)
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    if (ticket.getAssignee() == null) {
      throw new RuntimeException("Ticket não possui responsável");
    }

    if (!ticket.getAssignee().getId().equals(user.getId())) {
      throw new RuntimeException("Ticket não assumido pelo usuário");
    }

    ticket.setPriority(priority);

    Ticket save = ticketRepository.save(ticket);
    History history = new History(
        null,
        save,
        user,
        "Prioridade alterada para " + priority,
        LocalDateTime.now());
        historyRepository.save(history);
    return new TicketResponse(
        save.getId(),
        save.getTitle(),
        save.getDescription(),
        save.getTicketStatus(),
        save.getPriority(),
        save.getUserRequester().getName(),
        save.getSectorApplicant(),
        save.getSectorDestiny(),
        save.getAssignee() != null
            ? save.getAssignee().getName()
            : null);
  }

  public TicketResponse findByIdTicket(Long idTicket) {
    Ticket ticket = ticketRepository.findById(idTicket)
        .orElseThrow(() -> new RuntimeException("ticket não encontrado!"));
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

  public List<TicketResponse> findAllTicket() {
    List<Ticket> lTickets = ticketRepository.findAll();
    List<TicketResponse> nList = new ArrayList<>();
    for (Ticket ticket : lTickets) {
      nList.add(new TicketResponse(ticket.getId(),
          ticket.getTitle(),
          ticket.getDescription(),
          ticket.getTicketStatus(),
          ticket.getPriority(),
          ticket.getUserRequester().getName(),
          ticket.getSectorApplicant(),
          ticket.getSectorDestiny(),
          ticket.getAssignee() != null ? ticket.getAssignee().getName() : null));
    }
    return nList;
  }

  public List<HistoryResponse> getHistory(Long idTicket) {

    ticketRepository.findById(idTicket)
        .orElseThrow(() -> new RuntimeException("Ticket não encontrado!"));

    List<History> histories = historyRepository.findByTicketIdOrderByCreatedAtAsc(idTicket);

    List<HistoryResponse> response = new ArrayList<>();

    for (History history : histories) {

      response.add(new HistoryResponse(
          history.getId(),
          history.getUser().getName(),
          history.getAction(),
          history.getCreatedAt()));
    }
    return response;
  }
}