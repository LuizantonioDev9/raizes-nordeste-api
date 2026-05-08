package com.testefinal.demofinal.infrastructure.integration.payment;

public class PaymentResponse {

    private boolean success;
    private String message;
    private String status;

    public PaymentResponse(boolean success, String message, String status) {
        this.success = success;
        this.message = message;
        this.status = status;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }
}
