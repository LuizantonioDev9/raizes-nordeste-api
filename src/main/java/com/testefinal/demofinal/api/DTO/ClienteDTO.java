package com.testefinal.demofinal.api.DTO;

import com.testefinal.demofinal.domain.model.Cliente;

public record ClienteDTO(String nome, String email, String telefone,String senha) {

    public Cliente mapearCliente() {
        Cliente cli = new Cliente();
        cli.setNome(this.nome);
        cli.setEmail(this.email);
        cli.setTelefone(this.telefone);
        cli.setSenha(this.senha);
        return cli;
    }
}
