package com.bobrov.service;

import com.bobrov.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    private final DatabaseService databaseService;

    public UserService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public String getUserName(String username, String password) throws SQLException {
        String sql = "SELECT username FROM people WHERE username = ? AND password = ?";
        ResultSet resultSet = databaseService.executePreparedQuery(sql, username, password);
        if (resultSet.next()) {
            return resultSet.getString("username") + " " + resultSet.getString("surname");
        }
        return null;
    }

    public void createUser(User user) throws SQLException {
        String sql = "INSERT INTO people (name, surname, birthday, sex, username, password, isAdmin) VALUES (?, ?, ?, ?, ?, ?, ?)";
        databaseService.executePreparedUpdate(sql,
                user.getName(),
                user.getSurname(),
                user.getBirthday().toString(),
                user.getSex(),
                user.getUsername(),
                user.getPassword(),
                user.getIsAdmin());
        System.out.println("User " + user.getUsername() + " created successfully.");
    }

    public User authenticateUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM people WHERE username = ? AND password = ?";
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

    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE people SET name = ?, surname = ?, birthday = ?, sex = ?, password = ? WHERE username = ?";
        databaseService.executePreparedUpdate(sql,
                user.getName(),
                user.getSurname(),
                user.getBirthday().toString(),
                user.getSex(),
                user.getPassword(),
                user.getUsername());
        System.out.println("User " + user.getUsername() + " updated successfully.");
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM people WHERE isAdmin = 0";
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

    public User getUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM people WHERE username = ?";
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
