package com.ilyap.yuta.models;

import lombok.Value;

@Value
public class ProjectDto {
    int id;
    String photo;
    String name;
    String technicalTask;
    String status;
    String stringDeadline;
    String description;
    User manager;
    Team team;
}