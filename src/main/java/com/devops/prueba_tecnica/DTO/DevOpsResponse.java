package com.devops.prueba_tecnica.DTO;

public class DevOpsResponse {

    private String message;

    public DevOpsResponse() {
    }

    public DevOpsResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
