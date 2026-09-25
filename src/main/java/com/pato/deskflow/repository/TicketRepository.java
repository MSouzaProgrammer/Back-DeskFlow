package com.pato.deskflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pato.deskflow.entidades.Ticket;
import com.pato.deskflow.enuns.Sector;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findAllByOrderByOpenedAtDesc();

    List<Ticket> findByRequesterEmailOrderByOpenedAtDesc(String email);

    List<Ticket> findBySectorOrderByOpenedAtDesc(Sector sector);
}