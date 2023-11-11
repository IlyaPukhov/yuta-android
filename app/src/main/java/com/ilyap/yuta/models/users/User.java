package com.ilyap.yuta.models.users;

import com.ilyap.yuta.models.teams.Team;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class User implements Serializable {
    private int id;
    private String photo;
    private String croppedPhoto;
    private String login;
    private String lastName;
    private String firstName;
    private String patronymic;
    private LocalDate birthday;
    private String phoneNumber;
    private String eMail;
    private String vk;
    private String biography;
    private Faculty faculty;
    private Direction direction;
    private Group group;
    private List<Team> teams;

    public User() {
    }

    public User(int id, String photo, String croppedPhoto, String login, String lastName, String firstName, String patronymic, LocalDate birthday, String phoneNumber, String eMail, String vk, String biography, Faculty faculty, Direction direction, Group group, List<Team> teams) {
        this.id = id;
        this.photo = photo;
        this.croppedPhoto = croppedPhoto;
        this.login = login;
        this.lastName = lastName;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
        this.eMail = eMail;
        this.vk = vk;
        this.biography = biography;
        this.faculty = faculty;
        this.direction = direction;
        this.group = group;
        this.teams = teams;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCroppedPhoto() {
        return croppedPhoto;
    }

    public void setCroppedPhoto(String croppedPhoto) {
        this.croppedPhoto = croppedPhoto;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
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

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }
}