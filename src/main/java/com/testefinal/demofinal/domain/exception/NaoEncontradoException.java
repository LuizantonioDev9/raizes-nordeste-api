package com.testefinal.demofinal.domain.exception;

//404
public class NaoEncontradoException extends RuntimeException {
    public NaoEncontradoException(String message) {
        super(message);
    }
}
