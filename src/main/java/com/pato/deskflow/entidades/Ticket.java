package com.pato.deskflow.entidades;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.pato.deskflow.enuns.Priority;
import com.pato.deskflow.enuns.Sector;
import com.pato.deskflow.enuns.TicketStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tb_tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus ticketStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "requester_id", nullable = false)
    private User userRequester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Sector sectorApplicant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Sector sectorDestiny;
    
    @Column(name = "opened_at", nullable = false)
    private LocalDateTime openedAt;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    @OneToMany(
        mappedBy = "ticket",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Message> messages = new ArrayList<>();

    public Ticket() {
    }

    public Ticket(Long id, String title, String description, TicketStatus ticketStatus, Priority priority,
        User userRequester, User assignee, Sector sectorApplicant, Sector sectorDestiny, LocalDateTime openedAt,
        LocalDateTime closedAt, List<Message> messages) {
      this.id = id;
      this.title = title;
      this.description = description;
      this.ticketStatus = ticketStatus;
      this.priority = priority;
      this.userRequester = userRequester;
      this.assignee = assignee;
      this.sectorApplicant = sectorApplicant;
      this.sectorDestiny = sectorDestiny;
      this.openedAt = openedAt;
      this.closedAt = closedAt;
      this.messages = messages;
    }

    
}