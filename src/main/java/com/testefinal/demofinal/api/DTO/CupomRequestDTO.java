package com.testefinal.demofinal.api.DTO;

import jakarta.validation.constraints.NotBlank;

public record CupomRequestDTO(
        @NotBlank(message = "Código no cupom é obrigatório")
        String codigo
)
{
}
