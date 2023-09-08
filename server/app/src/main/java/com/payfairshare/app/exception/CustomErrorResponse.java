package com.payfairshare.app.exception;

import java.time.OffsetDateTime;

import lombok.Data;

@Data
public class CustomErrorResponse {
    private String timestamp;
    private int status;
    private String error;
    private String path;

    public CustomErrorResponse() {
        this.timestamp = OffsetDateTime.now().toString();
    }

    public CustomErrorResponse(int status, String error, String path) {
        this();
        this.status = status;
        this.error = error;
        this.path = path;
    }
}