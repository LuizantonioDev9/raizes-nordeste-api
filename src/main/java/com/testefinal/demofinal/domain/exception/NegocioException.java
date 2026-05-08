package com.testefinal.demofinal.domain.exception;

//409
public class NegocioException extends RuntimeException {
    public NegocioException(String message) {
        super(message);
    }
}
