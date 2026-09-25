package com.pato.deskflow.entidades;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.pato.deskflow.enuns.Access;
import com.pato.deskflow.enuns.Sector;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tb_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String password;

    private Access access;

    private Sector sector;

    public User() {
    }

    public User(
            Long id,
            String name,
            String email,
            String password,
            Access access,
            Sector sector) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.access = access;
        this.sector = sector;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(
            new SimpleGrantedAuthority(
                "ROLE_" + access
            )
        );
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }
}