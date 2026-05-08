package com.testefinal.demofinal.infrastructure.repository;

import com.testefinal.demofinal.domain.model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, UUID> {

    void deleteById(UUID id);
}
