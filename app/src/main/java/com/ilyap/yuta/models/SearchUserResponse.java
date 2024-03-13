package com.ilyap.yuta.models;

import java.util.List;

import lombok.Value;

@Value
public class SearchUserResponse {
    List<User> users;
}