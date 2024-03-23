package com.ilyap.yuta.models;

import lombok.Value;

@Value
public class UserResponse {
    String status;
    String error;
    User user;
}