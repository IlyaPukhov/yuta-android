package com.ilyap.yuta.models;

import lombok.Value;

import java.util.List;

@Value
public class SearchTeamsResponse {
    String status;
    String error;
    List<Team> teams;
}