package com.testefinal.demofinal.api.DTO;

import java.time.LocalDateTime;

public class ErroValidacaoDTO {
    private String erro;
    private String message;
    private String timestamp;

    public ErroValidacaoDTO(String erro, String message) {
        this.erro = erro;
        this.message = message;
        this.timestamp = LocalDateTime.now().toString();
    }

    public String getErro() {return erro;}
    public String getMessage() {return message;}
    public String getTimestamp() {return timestamp;}


}
