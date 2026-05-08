package com.testefinal.demofinal.domain.exception;

//401
public class NaoAutorizadoException extends RuntimeException {
    public NaoAutorizadoException(String message) {
        super(message);
    }
}
