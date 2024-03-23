package com.ilyap.yuta.models;

import lombok.Value;

@Value
public class TeamResponse {
    String status;
    String error;
    Team team;
}