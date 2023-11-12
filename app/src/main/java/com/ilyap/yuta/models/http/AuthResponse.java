package com.ilyap.yuta.models.http;

public class AuthResponse {
    String status;
    Integer userId;

    public AuthResponse(String status, Integer userId) {
        this.status = status;
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}