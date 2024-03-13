package com.ilyap.yuta.models;

import lombok.Value;

import java.util.List;

@Value
public class SearchTeamResponse {
    String status;
    String error;
    List<Team> teams;
}