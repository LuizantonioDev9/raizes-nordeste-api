package com.testefinal.demofinal.api.DTO;

import com.testefinal.demofinal.domain.model.Unidade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record UnidadeDTO(
        @NotBlank(message = "O nome da cidade não pode está vazio.")
        String nome,
        @NotBlank(message = "O nome do endereço não pode está vazio.")
        String endereco,
        @NotBlank(message = "O nome da cidade não pode está vazio.")
        String cidade) {


    public Unidade mapearUnidade() {
        Unidade uni = new Unidade();
        uni.setNome(nome);
        uni.setEndereco(endereco);
        uni.setCidade(cidade);
        return uni;
    }

}
