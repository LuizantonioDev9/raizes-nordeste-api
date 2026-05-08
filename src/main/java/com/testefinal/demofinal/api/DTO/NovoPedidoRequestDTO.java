package com.testefinal.demofinal.api.DTO;

import com.testefinal.demofinal.domain.enums.CanalPedido;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record NovoPedidoRequestDTO(
        UUID unidadeId,
        @NotNull(message = "Canal de pedido não pode ser nulo")
        CanalPedido canalPedido,
        String codigoCupom,
        List<ItemPedidoRequestDTO> itens,
        Integer pontosResgate
) {
}
