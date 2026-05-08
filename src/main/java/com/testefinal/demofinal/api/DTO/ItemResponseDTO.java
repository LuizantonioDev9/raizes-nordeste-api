package com.testefinal.demofinal.api.DTO;

import java.math.BigDecimal;

public record ItemResponseDTO(
        String nomeProduto,
        Integer quantidade,
        BigDecimal valorProduto)
{}
