package com.testefinal.demofinal.infrastructure.repository;

import com.testefinal.demofinal.domain.model.Unidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UnidadeRepository extends JpaRepository<Unidade, UUID> {
    boolean existsByNome(String nome);

}
