package com.ilyap.yuta.models;

import lombok.Value;

import java.util.List;

@Value
public class SearchUserResponse {
    List<User> users;
}