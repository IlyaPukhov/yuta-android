package com.ilyap.yuta.models;

public class TeamMember {
    private Team team;
    private User member;

    public TeamMember(Team team, User member) {
        this.team = team;
        this.member = member;
    }

    public Team getTeam() {
        return team;
    }

    public User getMember() {
        return member;
    }
}