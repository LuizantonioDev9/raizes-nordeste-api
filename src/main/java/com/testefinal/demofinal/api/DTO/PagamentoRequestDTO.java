package com.testefinal.demofinal.api.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PagamentoRequestDTO(
        @NotNull(message = "O id do pedido é obrigatório")
        UUID pedidoId,
        boolean sucesso
) {
}
