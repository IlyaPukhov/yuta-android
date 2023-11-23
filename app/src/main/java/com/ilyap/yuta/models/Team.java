package com.ilyap.yuta.models;

import java.util.List;

public class Team {
    String name;
    User leader;
    List<User> members;

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