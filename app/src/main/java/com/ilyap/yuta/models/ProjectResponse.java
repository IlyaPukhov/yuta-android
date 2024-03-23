package com.ilyap.yuta.models;

import lombok.Value;

@Value
public class ProjectResponse {
    String status;
    String error;
    Project project;
}