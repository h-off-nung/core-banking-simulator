package com.bobrov.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Card {
    private static final Set<String> existingCardIds = new HashSet<>();

    private final String id;                       // Unique 9-digit card ID
    private final String ownerUsername;            // Owner's username
    private double amount;                         // Current balance
    private final CardType type;                   // Credit or Debit
    private final LocalDate registerDate;          // Date of card registration
    private final List<Transaction> transactions;  // List of transactions associated with this card
    private boolean isBlocked;                     // Card blocking status

    // Enum for CardType (Credit or Debit)
    public enum CardType {
        CREDIT,
        DEBIT
    }

    // Constructor
    public Card(String ownerUsername, double amount, CardType type) {
        this.id = Card.generateCardId();
        this.ownerUsername = ownerUsername;
        this.amount = amount;
        this.type = type;
        this.registerDate = LocalDate.now();
        this.transactions = new ArrayList<>();
        this.isBlocked = false;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public CardType getType() {
        return type;
    }

    public LocalDate getRegisterDate() {
        return registerDate;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    // Method to add a transaction to the card
    public void addTransaction(Transaction transaction) {
        if (this.isBlocked) {
            throw new IllegalStateException("Cannot perform transaction. Card is blocked.");
        }
        this.transactions.add(transaction);
    }

    // Method to block the card
    public void block() {
        this.isBlocked = true;
    }

    // Method to unblock the card
    public void unblock() {
        this.isBlocked = false;
    }

    // Static method to generate a unique 6-digit card ID
    public static String generateCardId() {
        String newId;
        do {
            newId = "C" + String.format("%08d", (int) (Math.random() * 100000000));
        } while (existingCardIds.contains(newId));
        existingCardIds.add(newId);
        return newId;
    }
}
