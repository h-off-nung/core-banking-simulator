package com.bobrov.model;

import java.time.LocalDate;

public class Admin extends User {

    public Admin(String name, String surname, LocalDate birthday, String sex, String username, String password) {
        super(name, surname, birthday, sex, username, password);
    }

    public int getIsAdmin() {
        return 1;
    }
}

