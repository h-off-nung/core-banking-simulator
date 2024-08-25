package com.bobrov.service;

import com.bobrov.model.Transaction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionService {

    private final DatabaseService databaseService;

    // Constructor
    public TransactionService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public void createTransaction(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (senderCardId, recipientCardId, dateTime, amount, type) VALUES (?, ?, ?, ?, ?)";
        databaseService.executePreparedUpdate(sql,
                transaction.getSenderCardId(),
                transaction.getRecipientCardId(),
                transaction.getDateTime().toString(),
                transaction.getAmount(),
                transaction.getType().name());
        System.out.println("Transaction created successfully.");
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
