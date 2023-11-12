package com.ilyap.yuta.models.teams;

import com.ilyap.yuta.models.users.User;

import java.io.Serializable;
import java.util.List;

public class Team implements Serializable {
    private int id;
    private String name;
    private User leader;
    private List<User> members;

    public Team(int id, String name, User leader, List<User> members) {
        this.id = id;
        this.name = name;
        this.leader = leader;
        this.members = members;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getLeader() {
        return leader;
    }

    public void setLeader(User leader) {
        this.leader = leader;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }
}