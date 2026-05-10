package com.testefinal.demofinal.api.DTO;

import com.testefinal.demofinal.domain.model.Produto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ItemPedidoRequestDTO(
        @NotNull(message = "Todos os itens do pedido devem conter o ID do produto")
        UUID produtoId,
        @NotNull(message = "A quantidade do item é obrigatória.")
        @Min(value = 1, message = "A quantidade deve ser pelo menos 1.")
        Integer quantidade) {
}
