package com.testefinal.demofinal.infrastructure.repository;

import com.testefinal.demofinal.domain.model.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EstoqueRepository extends JpaRepository<Estoque, UUID> {

    //procura um produto especifico em uma unidade especifica
    Optional<Estoque> findByProdutoIdAndUnidadeId(UUID produtoId, UUID unidadeId);
    List<Estoque> findByUnidadeId(UUID unidadeId);
    boolean existsByProdutoIdAndUnidadeId(UUID produtoId, UUID unidadeId);
}
