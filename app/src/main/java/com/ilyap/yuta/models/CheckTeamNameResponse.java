package com.ilyap.yuta.models;

import lombok.Value;

@Value
public class CheckTeamNameResponse {
    String status;
    String error;
    boolean unique;
}