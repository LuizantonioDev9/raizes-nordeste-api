package com.testefinal.demofinal.api.DTO;



import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PedidoResponseDTO(
        UUID pedidoId,
        UUID clienteId,
        String canalPedido,
        String status,
        BigDecimal totalPedido,
        String cupomCodigo,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao,
        List<ItemResponseDTO> itens
) {
}
