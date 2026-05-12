package com.testefinal.demofinal.api.DTO;

import java.math.BigDecimal;
import java.util.UUID;

public record ItemResponseDTO(
        UUID itemId,
        String nomeProduto,
        Integer quantidade,
        BigDecimal valorProduto)
{}
