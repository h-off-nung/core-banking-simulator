package com.bobrov.model;

import com.bobrov.service.CardService;
import com.bobrov.service.UserService;
import com.bobrov.service.TransactionService;

import java.time.LocalDate;
import java.util.List;

public class Admin extends User {

    // Constructor
    public Admin(String name, String surname, LocalDate birthday, String sex, String username, String password) {
        super(name, surname, birthday, sex, username, password);
    }

    // Method to block a user's bank card
    public void blockCard(String cardId, CardService bankCardService) {
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
    public void unblockCard(String cardId, CardService bankCardService) {
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
    public void viewAllUsers(UserService userService) {
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
    public void viewAllTransactions(TransactionService transactionService) {
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

