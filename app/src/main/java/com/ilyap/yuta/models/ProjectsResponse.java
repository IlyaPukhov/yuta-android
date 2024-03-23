package com.ilyap.yuta.models;

import lombok.Value;

import java.util.List;

@Value
public class ProjectsResponse {
    List<ProjectDto> managedProjects;
    List<ProjectDto> othersProjects;
}