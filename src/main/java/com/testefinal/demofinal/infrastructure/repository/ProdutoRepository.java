package com.testefinal.demofinal.infrastructure.repository;

import com.testefinal.demofinal.domain.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProdutoRepository extends JpaRepository<Produto, UUID> {


}
