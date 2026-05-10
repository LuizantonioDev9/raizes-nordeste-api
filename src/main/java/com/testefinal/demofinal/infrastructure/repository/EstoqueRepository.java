package com.testefinal.demofinal.infrastructure.repository;

import com.testefinal.demofinal.domain.model.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EstoqueRepository extends JpaRepository<Estoque, UUID> {

    Optional<Estoque> findByProdutoIdAndUnidadeId(UUID produtoId, UUID unidadeId);
    List<Estoque> findByUnidadeId(UUID unidadeId);
    boolean existsByProdutoIdAndUnidadeId(UUID produtoId, UUID unidadeId);
}
