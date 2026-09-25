package com.pato.deskflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pato.deskflow.entidades.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

}