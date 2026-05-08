package com.testefinal.demofinal.api.DTO;

import java.util.UUID;

public record EstoqueRequestDTO(UUID produtoId, UUID unidadeId, Integer quantidade) {
}
