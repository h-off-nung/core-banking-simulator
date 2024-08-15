package com.bobrov.service;

import com.bobrov.model.Card;
import com.bobrov.model.Transaction;
import com.bobrov.model.User;

import java.sql.SQLException;
import java.util.List;

public class AdminService {

    private final UserService userService;
    private final CardService bankCardService;
    private final TransactionService transactionService;

    // Constructor
    public AdminService(UserService userService, CardService bankCardService, TransactionService transactionService) {
        this.userService = userService;
        this.bankCardService = bankCardService;
        this.transactionService = transactionService;
    }

    // Method to block a user's bank card
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

    // Method to unblock a user's bank card
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

    // Method to view all users in the system
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

    // Method to view all transactions in the system
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
