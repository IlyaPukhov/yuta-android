package com.ilyap.yuta.models;

import lombok.Value;

import java.util.List;

@Value
public class Team {
    int id;
    String name;
    User leader;
    List<User> members;
}