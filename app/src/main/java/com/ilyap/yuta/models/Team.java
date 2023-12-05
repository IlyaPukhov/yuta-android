package com.ilyap.yuta.models;

import java.util.List;

public class Team {
    private int id;
    private String name;
    private User leader;
    private List<User> members;

    public int getId() {
        return id;
    }

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