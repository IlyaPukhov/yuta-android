package com.ilyap.yuta.models;

import java.util.List;

import lombok.Value;

@Value
public class TeamResponse {
    List<Team> managedTeams;
    List<Team> othersTeams;
}