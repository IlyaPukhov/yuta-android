package com.ilyap.yuta.models;

import java.util.List;

public class TeamResponse {
    List<Team> managedTeams;
    List<Team> othersTeams;

    public List<Team> getManagedTeams() {
        return managedTeams;
    }

    public List<Team> getOthersTeams() {
        return othersTeams;
    }
}