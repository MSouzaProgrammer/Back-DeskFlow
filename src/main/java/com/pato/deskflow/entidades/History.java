package com.pato.deskflow.entidades;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tb_history")
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String action;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public History() {}

    public History(Long id, Ticket ticket, User user, String action, LocalDateTime createdAt) {
        this.id = id;
        this.ticket = ticket;
        this.user = user;
        this.action = action;
        this.createdAt = createdAt;
    }
}