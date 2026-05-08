package com.testefinal.demofinal.api.DTO;

import com.testefinal.demofinal.domain.model.Cupom;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CupomResponseDTO(
        UUID id,
        String codigo,
        String tipoDesconto,
        BigDecimal preco,
        LocalDateTime validade
) {
}
