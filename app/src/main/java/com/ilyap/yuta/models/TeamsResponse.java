package com.ilyap.yuta.models;

import lombok.Value;

import java.util.List;

@Value
public class TeamsResponse {
    String status;
    String error;
    List<Team> managedTeams;
    List<Team> othersTeams;
}