package com.bobrov.service;

import com.bobrov.model.Card;
import com.bobrov.model.Transaction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionService {

    private final DatabaseService databaseService;

    public TransactionService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public void performTransaction(Card senderCard, Card recipientCard, String recipientCardId, double amount, Transaction.TransactionType type, CardService cardService) throws SQLException {
        if (senderCard.isBlocked()) {
            System.out.println("Sender card is blocked.");
            return;
        } else if (recipientCard != null && recipientCard.isBlocked()) {
            System.out.println("Recipient card is blocked.");
            return;
        }
        double newSenderBalance = senderCard.getAmount() - amount;
        senderCard.setAmount(newSenderBalance);
        cardService.updateCard(senderCard);

        if (type == Transaction.TransactionType.USER_TO_USER_SAME_BANK) {
            // If recipient card exists in the database, update the recipient card's balance
            double newRecipientBalance = recipientCard.getAmount() + amount;
            recipientCard.setAmount(newRecipientBalance);
            cardService.updateCard(recipientCard);
        }
        System.out.println("Transaction performed successfully.");
        Transaction transaction = new Transaction(senderCard.getId(), recipientCardId, amount, type);
        createTransaction(transaction);
        System.out.println("Transaction saved successfully.");
    }
    private void createTransaction(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (senderCardId, recipientCardId, dateTime, amount, type) VALUES (?, ?, ?, ?, ?)";
        databaseService.executePreparedUpdate(sql,
                transaction.getSenderCardId(),
                transaction.getRecipientCardId(),
                transaction.getDateTime().toString(),
                transaction.getAmount(),
                transaction.getType().name());
    }

    public List<Transaction> getTransactionsByCardId(String cardId) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE senderCardId = ? OR recipientCardId = ?";
        ResultSet resultSet = databaseService.executePreparedQuery(sql, cardId, cardId);

        while (resultSet.next()) {
            transactions.add(new Transaction(
                    resultSet.getString("senderCardId"),
                    resultSet.getString("recipientCardId"),
                    resultSet.getDouble("amount"),
                    Transaction.TransactionType.valueOf(resultSet.getString("type"))
            ));
        }

        return transactions;
    }

    public List<Transaction> getAllTransactions() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions";
        ResultSet resultSet = databaseService.executeQuery(sql);

        while (resultSet.next()) {
            transactions.add(new Transaction(
                    resultSet.getString("senderCardId"),
                    resultSet.getString("recipientCardId"),
                    resultSet.getDouble("amount"),
                    Transaction.TransactionType.valueOf(resultSet.getString("type"))
            ));
        }

        return transactions;
    }
}
