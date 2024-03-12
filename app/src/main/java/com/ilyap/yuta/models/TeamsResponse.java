package com.ilyap.yuta.models;

import java.util.List;

import lombok.Value;

@Value
public class TeamsResponse {
    List<Team> managedTeams;
    List<Team> othersTeams;
}