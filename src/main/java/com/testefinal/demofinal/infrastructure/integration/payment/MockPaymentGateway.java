package com.testefinal.demofinal.infrastructure.integration.payment;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class MockPaymentGateway implements PaymentGateway {

    @Override
    public PaymentResponse processPayment(Double value) {
        if (value > 1000) {
            return new PaymentResponse(
                    false,
                    "RECUSADO",
                    "Valor acima do limite"
            );
        }

        if(value > 500) {
            boolean aprovado = new Random().nextBoolean();

            if(!aprovado) {
                return new PaymentResponse(
                        false,
                        "RECUSADO",
                        "Pagamento não autorizado"
                );
            }
        }

        return new PaymentResponse(
                true,
                "APROVADO",
                "Pagamento realizado com sucesso"
        );
    }
}
