package com.testefinal.demofinal.api.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CupomResponseDTO(
        UUID id,
        String codigo,
        com.testefinal.demofinal.domain.enums.TipoDesconto tipoDesconto,
        BigDecimal preco,
        LocalDateTime validade
) {
}
