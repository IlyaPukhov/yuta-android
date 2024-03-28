package com.ilyap.yuta.models;

import lombok.Value;

import java.util.List;

@Value
public class SearchUsersResponse {
    String status;
    String error;
    List<User> users;
}