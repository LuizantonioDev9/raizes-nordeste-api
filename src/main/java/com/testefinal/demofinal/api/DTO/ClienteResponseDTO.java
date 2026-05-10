package com.testefinal.demofinal.api.DTO;

import java.util.UUID;

public record ClienteResponseDTO(
        UUID clienteId,
        String nomeCliente,
        String emailCliente,
        String telefoneCliente) {
}
