package com.bobrov.service;

import com.bobrov.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    private final DatabaseService databaseService;

    // Constructor
    public UserService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    // Method to create a new user
    public void createUser(User user) throws SQLException {
        String sql = "INSERT INTO users (name, surname, birthday, sex, username, password) VALUES (?, ?, ?, ?, ?, ?)";
        databaseService.executePreparedUpdate(sql,
                user.getName(),
                user.getSurname(),
                user.getBirthday().toString(),
                user.getSex(),
                user.getUsername(),
                user.getPassword());
        System.out.println("User " + user.getUsername() + " created successfully.");
    }

    // Method to authenticate a user
    public User authenticateUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        ResultSet resultSet = databaseService.executePreparedQuery(sql, username, password);

        if (resultSet.next()) {
            return new User(
                    resultSet.getString("name"),
                    resultSet.getString("surname"),
                    LocalDate.parse(resultSet.getString("birthday")),
                    resultSet.getString("sex"),
                    resultSet.getString("username"),
                    resultSet.getString("password")
            );
        } else {
            return null;
        }
    }

    // Method to update a user's information
    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE users SET name = ?, surname = ?, birthday = ?, sex = ?, password = ? WHERE username = ?";
        databaseService.executePreparedUpdate(sql,
                user.getName(),
                user.getSurname(),
                user.getBirthday().toString(),
                user.getSex(),
                user.getPassword(),
                user.getUsername());
        System.out.println("User " + user.getUsername() + " updated successfully.");
    }

    // Method to retrieve all users
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        ResultSet resultSet = databaseService.executeQuery(sql);

        while (resultSet.next()) {
            users.add(new User(
                    resultSet.getString("name"),
                    resultSet.getString("surname"),
                    LocalDate.parse(resultSet.getString("birthday")),
                    resultSet.getString("sex"),
                    resultSet.getString("username"),
                    resultSet.getString("password")
            ));
        }

        return users;
    }

    // Method to find a user by username
    public User getUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        ResultSet resultSet = databaseService.executePreparedQuery(sql, username);

        if (resultSet.next()) {
            return new User(
                    resultSet.getString("name"),
                    resultSet.getString("surname"),
                    LocalDate.parse(resultSet.getString("birthday")),
                    resultSet.getString("sex"),
                    resultSet.getString("username"),
                    resultSet.getString("password")
            );
        } else {
            return null;
        }
    }
}
