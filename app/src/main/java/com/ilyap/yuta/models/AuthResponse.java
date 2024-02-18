package com.ilyap.yuta.models;

import lombok.Value;

@Value
public class AuthResponse {
    String status;
    Integer userId;
}