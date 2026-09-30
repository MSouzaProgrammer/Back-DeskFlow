package com.pato.deskflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pato.deskflow.entidades.History;

public interface HistoryRepository extends JpaRepository<History, Long> {

    List<History> findByTicketIdOrderByCreatedAtAsc(Long ticketId);
}