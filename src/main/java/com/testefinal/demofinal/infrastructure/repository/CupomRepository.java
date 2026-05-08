package com.testefinal.demofinal.infrastructure.repository;

import com.testefinal.demofinal.domain.model.Cupom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CupomRepository extends JpaRepository<Cupom, UUID> {
    Optional<Cupom> findByCodigo(String codigo);
    List<Cupom> findByValidadeAfter(LocalDateTime dataAtual);
}
