package com.ilyap.yuta.models;

import lombok.Value;

@Value
public class ProjectDto {
    int id;
    String photoUrl;
    String name;
    String technicalTaskUrl;
    String status;
    String stringDeadline;
    String description;
    User manager;
    Team team;
}