package com.testefinal.demofinal.infrastructure.integration.log;

import org.springframework.stereotype.Service;

@Service
public class LogService {
    public void info(String message) {
        System.out.println("[INFO]" + message);
    }
}
