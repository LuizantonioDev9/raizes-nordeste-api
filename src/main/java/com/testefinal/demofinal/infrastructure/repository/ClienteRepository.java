package com.testefinal.demofinal.infrastructure.repository;

import com.testefinal.demofinal.domain.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {

    Optional<Cliente> findById(UUID id);
    Optional<Cliente> findByEmail(String email);

}
