package com.testefinal.demofinal.api.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProdutoRequestDTO(
        @NotBlank(message = "O nome do produto não pode estar vazio")
        String nome,

        @NotNull(message = "O preço é obrigatorio")
        BigDecimal preco
) {
}
