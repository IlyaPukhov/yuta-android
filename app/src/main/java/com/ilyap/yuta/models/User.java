package com.ilyap.yuta.models;

import lombok.Data;

@Data
public class User {
    private int id;
    private String login;
    private String photoUrl;
    private String croppedPhotoUrl;
    private String lastName;
    private String firstName;
    private String patronymic;
    private String age;
    private String biography;
    private String phoneNumber;
    private String eMail;
    private String vk;
    private String faculty;
    private String direction;
    private String group;
    private int allProjectsCount;
    private int doneProjectsCount;
    private int allTasksCount;
    private int doneTasksCount;
    private int teamsCount;
}