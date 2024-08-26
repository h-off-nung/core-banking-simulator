package com.bobrov.model;

import com.bobrov.service.CardService;
import com.bobrov.service.UserService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private final String surname;
    private final LocalDate birthday;
    private final String sex;
    private String username;
    private String password;
    private final List<Card> cards;

    public User(String name, String surname, LocalDate birthday, String sex, String username, String password) {
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.sex = sex;
        this.username = username;
        this.password = password;
        this.cards = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public int getIsAdmin() {
        return 0;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getSex() {
        return sex;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static User signUp(String name, String surname, LocalDate birthday, String sex, String username, String password) {
        return new User(name, surname, birthday, sex, username, password);
    }
}

