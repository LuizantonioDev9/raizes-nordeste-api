package com.testefinal.demofinal.api.DTO;

import com.testefinal.demofinal.domain.enums.CanalPedido;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;
import java.util.UUID;

public record NovoPedidoRequestDTO(
        @NotNull(message = "A unidade do pedido é obrigatória")
        UUID unidadeId,
        @NotNull(message = "Canal de pedido não pode ser nulo")
        CanalPedido canalPedido,
        String codigoCupom,

        @NotNull(message = "A lista de itens não pode ser nula.")
        @NotEmpty(message = "O pedido está vazio. Adicione um item.")
        @Valid
        List<ItemPedidoRequestDTO> itens,
        @PositiveOrZero(message = "Os pontos de resgate não podem ser negativos.")
        Integer pontosResgate
) {
}
