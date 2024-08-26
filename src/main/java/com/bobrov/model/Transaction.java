package com.bobrov.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Transaction {
    private static final Set<String> existingTransactionIds = new HashSet<>();

    private final String id;
    private final String senderCardId;       // ID of the sender's bank card
    private final String recipientCardId;    // ID of the recipient's bank card (or ATM ID for withdrawals)
    private final LocalDateTime dateTime;    // Date and time of the transaction
    private final double amount;             // Transaction amount
    private final TransactionType type;      // Type of transaction (User to User, Withdrawal, etc.)


    // Enum for TransactionType
    public enum TransactionType {
        USER_TO_USER_SAME_BANK,
        USER_TO_USER_DIFFERENT_BANK,
        WITHDRAWAL
    }

    // Constructor
    public Transaction(String senderCardId, String recipientCardId, double amount, TransactionType type) {
        this.id = generateTransactionId();
        this.senderCardId = senderCardId;
        this.recipientCardId = recipientCardId;
        this.amount = amount;
        this.type = type;
        this.dateTime = LocalDateTime.now();
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public String getSenderCardId() {
        return senderCardId;
    }

    public String getRecipientCardId() {
        return recipientCardId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public double getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public String logTransaction() {
        return "|----------------------------------------------------------------------------------| \n" +
                "| Transaction Details: \n" +
                "|---------------------------------------------------------------------------------| \n" +
                "| Sender Card ID:  " + String.format("%-20s", senderCardId) + " | Recipient Card ID: " + String.format("%-20s", recipientCardId) + "\n" +
                "| Date and Time:    " + String.format("%-20s", dateTime.toString()) + " | Amount: " + String.format("%-20s", amount) + "\n" +
                "| Type:            " + String.format("%-20s", type) + "\n" +
                "|---------------------------------------------------------------------------------|";
    }

    // Static method to generate a unique 9-digit card ID
    public static String generateTransactionId() {
        String newId;
        do {
            newId = "T" + String.format("%08d", (int) (Math.random() * 100000000));
        } while (existingTransactionIds.contains(newId));
        existingTransactionIds.add(newId);
        return newId;
    }
}
