package com.testefinal.demofinal.api.DTO;

import java.util.UUID;

public record UsuarioResumoDTO(
        UUID id,
        String nome,
        String perfil
) {
}
