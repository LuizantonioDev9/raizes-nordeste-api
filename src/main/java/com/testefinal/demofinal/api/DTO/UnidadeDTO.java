package com.testefinal.demofinal.api.DTO;

import com.testefinal.demofinal.domain.model.Unidade;


public record UnidadeDTO(String nome, String endereco, String cidade) {


    public Unidade mapearUnidade() {
        Unidade uni = new Unidade();
        uni.setNome(nome);
        uni.setEndereco(endereco);
        uni.setCidade(cidade);
        return uni;
    }

}
