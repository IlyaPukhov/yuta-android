package com.ilyap.yuta.models;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getCroppedPhotoUrl() {
        return croppedPhotoUrl;
    }

    public void setCroppedPhotoUrl(String croppedPhotoUrl) {
        this.croppedPhotoUrl = croppedPhotoUrl;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getAge() {
        return age;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getVk() {
        return vk;
    }

    public void setVk(String vk) {
        this.vk = vk;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getDirection() {
        return direction;
    }

    public String getGroup() {
        return group;
    }

    public int getAllProjectsCount() {
        return allProjectsCount;
    }

    public int getDoneProjectsCount() {
        return doneProjectsCount;
    }

    public int getAllTasksCount() {
        return allTasksCount;
    }

    public int getDoneTasksCount() {
        return doneTasksCount;
    }

    public int getTeamsCount() {
        return teamsCount;
    }
}