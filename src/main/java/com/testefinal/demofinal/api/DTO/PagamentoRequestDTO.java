package com.testefinal.demofinal.api.DTO;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record PagamentoRequestDTO(
        @NotBlank(message = "O id do pedido é obrigatório")
        UUID pedidoId,
        boolean sucesso
) {
}
