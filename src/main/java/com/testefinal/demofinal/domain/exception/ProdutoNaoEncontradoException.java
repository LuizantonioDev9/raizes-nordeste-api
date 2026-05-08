package com.testefinal.demofinal.domain.exception;

//404
public class ProdutoNaoEncontradoException extends NaoEncontradoException {

    public ProdutoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

}
