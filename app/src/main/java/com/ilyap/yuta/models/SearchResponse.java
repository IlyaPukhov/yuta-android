package com.ilyap.yuta.models;

import java.util.List;

import lombok.Value;

@Value
public class SearchResponse {
    List<User> users;
}