package com.bobrov.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Card {
    private static final Set<String> existingCardIds = new HashSet<>();

    private final String id;                       // Unique 9-digit card ID
    private final String ownerUsername;
    private double amount;                         // Current balance
    private final CardType type;
    private final LocalDate registerDate;
    private final List<Transaction> transactions;
    private boolean isBlocked;

    // Enum for CardType (Credit or Debit)
    public enum CardType {
        CREDIT,
        DEBIT
    }

    public Card(String ownerUsername, double amount, CardType type) {
        this.id = generateCardId();
        this.ownerUsername = ownerUsername;
        this.amount = amount;
        this.type = type;
        this.registerDate = LocalDate.now();
        this.transactions = new ArrayList<>();
        this.isBlocked = false;
    }

    public Card(String id, String ownerUsername, double amount, CardType type) {
        this.id = id;
        this.ownerUsername = ownerUsername;
        this.amount = amount;
        this.type = type;
        this.registerDate = LocalDate.now();
        this.transactions = new ArrayList<>();
        this.isBlocked = false;
    }

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

    public boolean isBlocked() {
        return isBlocked;
    }

    public void block() {
        this.isBlocked = true;
    }

    public void unblock() {
        this.isBlocked = false;
    }

    // Static method to generate a unique 9-digit card ID
    public static String generateCardId() {
        String newId;
        do {
            newId = "C" + String.format("%08d", (int) (Math.random() * 100000000));
        } while (existingCardIds.contains(newId));
        existingCardIds.add(newId);
        return newId;
    }
}
