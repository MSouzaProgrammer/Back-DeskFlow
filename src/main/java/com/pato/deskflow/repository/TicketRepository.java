package com.pato.deskflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.pato.deskflow.entidades.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}