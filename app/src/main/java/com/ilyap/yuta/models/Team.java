package com.ilyap.yuta.models;

import java.util.List;

public class Team {
    private String name;
    private User leader;
    private List<User> members;

    public String getName() {
        return name;
    }

    public User getLeader() {
        return leader;
    }

    public List<User> getMembers() {
        return members;
    }
}