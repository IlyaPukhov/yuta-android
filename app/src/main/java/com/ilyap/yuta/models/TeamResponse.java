package com.ilyap.yuta.models;

import java.util.List;

public class TeamResponse {
    private List<Team> managedTeams;
    private List<Team> othersTeams;

    public List<Team> getManagedTeams() {
        return managedTeams;
    }

    public List<Team> getOthersTeams() {
        return othersTeams;
    }
}