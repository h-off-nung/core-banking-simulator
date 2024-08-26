package com.bobrov.service;

import com.bobrov.model.Admin;
import com.bobrov.model.Card;
import com.bobrov.model.Transaction;
import com.bobrov.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AdminService {

    private final DatabaseService databaseService;
    private final UserService userService;
    private final CardService bankCardService;
    private final TransactionService transactionService;

    // Constructor
    public AdminService(DatabaseService databaseService, UserService userService, CardService bankCardService, TransactionService transactionService) {
        this.databaseService = databaseService;
        this.userService = userService;
        this.bankCardService = bankCardService;
        this.transactionService = transactionService;
    }

    public void createAdmin(Admin admin) throws SQLException {
        String sql = "INSERT INTO people (name, surname, birthday, sex, username, password, isAdmin) VALUES (?, ?, ?, ?, ?, ?, ?)";
        databaseService.executePreparedUpdate(sql,
                admin.getName(),
                admin.getSurname(),
                admin.getBirthday().toString(),
                admin.getSex(),
                admin.getUsername(),
                admin.getPassword(),
                admin.getIsAdmin());
        System.out.println("Admin " + admin.getUsername() + " created successfully.");
    }

    public int isPersonAdmin(String username, String password) throws SQLException {
        String sql = "SELECT * FROM people WHERE username = ? AND password = ?";
        ResultSet resultSet = databaseService.executePreparedQuery(sql, username, password);
        if (resultSet.next()) {
            if (resultSet.getInt("isAdmin") == 1) {
                return 1;
            } else if (resultSet.getInt("isAdmin") == 0) {
                return 0;
            }
        }
        return 2;
    }

    public Admin authenticateAdmin(String username, String password) throws SQLException {
        String sql = "SELECT * FROM people WHERE username = ? AND password = ?";
        ResultSet resultSet = databaseService.executePreparedQuery(sql, username, password);
        if (resultSet.next()) {
            return new Admin(
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



    public void blockCard(String cardId) throws SQLException {
        Card card = bankCardService.getCardById(cardId);
        if (card != null) {
            card.block();
            bankCardService.updateCard(card);
            System.out.println("Card with ID " + cardId + " has been blocked.");
        } else {
            System.out.println("Card with ID " + cardId + " not found.");
        }
    }

    public void unblockCard(String cardId) throws SQLException {
        Card card = bankCardService.getCardById(cardId);
        if (card != null) {
            card.unblock();
            bankCardService.updateCard(card);
            System.out.println("Card with ID " + cardId + " has been unblocked.");
        } else {
            System.out.println("Card with ID " + cardId + " not found.");
        }
    }

    public void viewAllUsers() throws SQLException {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users found.");
        } else {
            users.forEach(user -> {
                System.out.println("Username: " + user.getUsername() + ", Name: " + user.getName() + " " + user.getSurname());
            });
        }
    }

    public void viewAllAdmins() throws SQLException {
        List<Admin> admins = getAllAdmins();
        if (admins.isEmpty()) {
            System.out.println("No admins found.");
        } else {
            admins.forEach(admin -> {
                System.out.println("Username: " + admin.getUsername() + ", Name: " + admin.getName() + " " + admin.getSurname());
            });
        }
    }

    public List<Admin> getAllAdmins() throws SQLException {
        List<Admin> admins = new ArrayList<>();
        String sql = "SELECT * FROM people WHERE isAdmin = 1";
        ResultSet resultSet = databaseService.executeQuery(sql);

        while (resultSet.next()) {
            admins.add(new Admin(
                    resultSet.getString("name"),
                    resultSet.getString("surname"),
                    LocalDate.parse(resultSet.getString("birthday")),
                    resultSet.getString("sex"),
                    resultSet.getString("username"),
                    resultSet.getString("password")
            ));
        }

        return admins;
    }

    public void viewAllTransactions() throws SQLException {
        List<Transaction> transactions = transactionService.getAllTransactions();
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            transactions.forEach(transaction -> {
                System.out.println(transaction.logTransaction());
            });
        }
    }
}
