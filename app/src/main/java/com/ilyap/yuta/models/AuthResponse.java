package com.ilyap.yuta.models;

import lombok.Value;

@Value
public class AuthResponse {
    String status;
    String error;
    Integer userId;
}