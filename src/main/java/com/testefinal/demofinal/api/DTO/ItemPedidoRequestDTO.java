package com.testefinal.demofinal.api.DTO;

import com.testefinal.demofinal.domain.model.Produto;

import java.util.UUID;

public record ItemPedidoRequestDTO(UUID produtoId, int quantidade) {
}
