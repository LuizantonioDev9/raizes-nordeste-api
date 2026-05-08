package com.testefinal.demofinal.api.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PagamentoResponseDTO(
        UUID pagamentoId,
        UUID pedidoId,
        String status,
        String metodo,
        BigDecimal valor,
        String payloadRetorno,
        LocalDateTime dataPagamento
) {
}
