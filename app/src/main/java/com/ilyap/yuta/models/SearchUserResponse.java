package com.ilyap.yuta.models;

import lombok.Value;

import java.util.List;

@Value
public class SearchUserResponse {
    String status;
    String error;
    List<User> users;
}