package com.testefinal.demofinal.api.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record EstoqueRequestDTO(
        @NotNull UUID produtoId,
        @NotNull UUID unidadeId,
        @Positive(message = "A quantidade deve ser maior que zero") Integer quantidade) {
}
