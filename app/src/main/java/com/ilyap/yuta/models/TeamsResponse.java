package com.ilyap.yuta.models;

import lombok.Value;

import java.util.List;

@Value
public class TeamsResponse {
    List<Team> managedTeams;
    List<Team> othersTeams;
}