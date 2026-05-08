package com.testefinal.demofinal.infrastructure.integration.email;

import org.springframework.stereotype.Service;

@Service
public class MockEmailService implements EmailService {

    @Override
    public void sendEmail(String to, String message) {
        System.out.println("Enviando email para " + to);
    }
}
