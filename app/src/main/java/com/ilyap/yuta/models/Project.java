package com.ilyap.yuta.models;

import lombok.Value;

@Value
public class Project {
    int id;
    String name;
    String technicalTaskUrl;
    String technicalTaskName;
    String deadline;
    String status;
    String description;
    Team team;
}