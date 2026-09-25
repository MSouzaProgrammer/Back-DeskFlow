package com.pato.deskflow.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pato.deskflow.entidades.User;

public interface UserRepository
        extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}