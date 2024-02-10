package com.ilyap.yuta.models;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class TeamMember {
    Team team;
    User member;
}