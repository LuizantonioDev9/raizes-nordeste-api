package com.testefinal.demofinal.domain.exception;

//409
public class ConflitoException extends RuntimeException {
    public ConflitoException(String message) {
        super(message);
    }
}
