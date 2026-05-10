package com.testefinal.demofinal.infrastructure.integration.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;



@Service
public class LogService {
    private static final Logger logger = LoggerFactory.getLogger(LogService.class);

    public void auditoria(String mensagem) {
        logger.info("[AUDITORIA] {}", mensagem);
    }

    public void alerta(String mensagem) {
        logger.warn("[AUDITORIA] {}", mensagem);
    }

    public void erro(String mensagem, Exception ex) {
        logger.error("[AUDITORIA] {}", mensagem, ex);
    }

}
