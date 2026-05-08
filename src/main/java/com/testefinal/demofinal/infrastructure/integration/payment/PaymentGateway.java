package com.testefinal.demofinal.infrastructure.integration.payment;

public interface PaymentGateway {
    PaymentResponse processPayment(Double value);
}