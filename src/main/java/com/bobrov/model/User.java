package com.bobrov.model;

import com.bobrov.service.UserService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String surname;
    private LocalDate birthday;
    private String sex;
    private String username;
    private String password;
    private final List<Card> cards;

    // Constructor
    public User(String name, String surname, LocalDate birthday, String sex, String username, String password) {
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.sex = sex;
        this.username = username;
        this.password = password;
        this.cards = new ArrayList<>();
    }

    // Getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public List<Card> getCards() {
        return cards;
    }

    public void addCard(Card card) {
        if (this.cards.size() < 3) {
            this.cards.add(card);
        } else {
            throw new IllegalStateException("Maximum number of cards (3) already registered.");
        }
    }

    // Method to perform a transaction
    public void performTransaction(Transaction transaction) {
        // Implement transaction logic here, such as checking daily limit, etc.
    }

    // Method to view user's transactions
    public List<Transaction> getTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        for (Card card : this.cards) {
            transactions.addAll(card.getTransactions());
        }
        return transactions;
    }

    // Method to view statistics for the transactions
    public String getStatistics() {
        // Implement statistics logic here, like amount and transactions number of a month
        return "Statistics";
    }

    // Method to sign up a new user (static factory method)
    public static User signUp(String name, String surname, LocalDate birthday, String sex, String username, String password) {
        return new User(name, surname, birthday, sex, username, password);
    }

    // Method to log in an existing user
    public static User logIn(String username, String password, UserService userService) throws SQLException {
        User user = userService.authenticateUser(username, password);
        if (user == null) {
            throw new IllegalArgumentException("Invalid username or password.");
        }
        return user;
    }
}

