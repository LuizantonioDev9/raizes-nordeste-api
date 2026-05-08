package com.testefinal.demofinal.domain.exception;

//409
public class EstoqueInsuficienteException extends NegocioException {
    public EstoqueInsuficienteException(String message) {
        super(message);
    }
}
