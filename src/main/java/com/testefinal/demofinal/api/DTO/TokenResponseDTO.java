package com.testefinal.demofinal.api.DTO;

public record TokenResponseDTO(
        String accessToken,
        String tokenType,
        int expiresIn,
        UsuarioResumoDTO user
) {
}
