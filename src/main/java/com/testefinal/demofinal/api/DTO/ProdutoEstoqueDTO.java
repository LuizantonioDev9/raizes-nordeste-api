package com.testefinal.demofinal.api.DTO;

import java.math.BigDecimal;
import java.util.UUID;

public record ProdutoEstoqueDTO(
        UUID id,
        String nome,
        String descricao,
        BigDecimal preco,
        Integer quantidadeEstoque
) {
}
