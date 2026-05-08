package com.testefinal.demofinal.api.DTO;

import java.util.List;

public record CatalogoUnidadeResponseDTO(
        ResumoUnidadeDTO unidade,
        List<ProdutoEstoqueDTO> produtos
) {
}
