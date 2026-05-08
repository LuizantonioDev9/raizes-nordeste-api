package com.testefinal.demofinal.api.DTO;

import java.util.UUID;

public record ClienteResponseDTO(UUID id, String nome, String email, String telefone) {
}
