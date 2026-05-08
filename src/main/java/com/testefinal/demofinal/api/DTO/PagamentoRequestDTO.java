package com.testefinal.demofinal.api.DTO;

import java.util.UUID;

public record PagamentoRequestDTO(
        UUID pedidoId,
        boolean sucesso
) {
}
