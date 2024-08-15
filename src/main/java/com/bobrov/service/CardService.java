package com.bobrov.service;

import com.bobrov.model.Card;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CardService {

    private final DatabaseService databaseService;

    // Constructor
    public CardService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    // Method to create a new bank card and store it in the database
    public void createCard(Card card) throws SQLException {
        String sql = "INSERT INTO bank_cards (id, ownerUsername, amount, type, registerDate, isBlocked) VALUES (?, ?, ?, ?, ?, ?)";
        databaseService.executePreparedUpdate(sql,
                card.getId(),
                card.getOwnerUsername(),
                card.getAmount(),
                card.getType().name(),
                card.getRegisterDate().toString(),
                card.isBlocked() ? 1 : 0);
        System.out.println("Bank card " + card.getId() + " created successfully.");
    }

    // Method to retrieve a bank card by its ID
    public Card getCardById(String cardId) throws SQLException {
        String sql = "SELECT * FROM bank_cards WHERE id = ?";
        ResultSet resultSet = databaseService.executePreparedQuery(sql, cardId);

        if (resultSet.next()) {
            return new Card(
                    resultSet.getString("ownerUsername"),
                    resultSet.getDouble("amount"),
                    Card.CardType.valueOf(resultSet.getString("type"))
            );
        } else {
            return null;
        }
    }

    // Method to update an existing bank card in the database
    public void updateCard(Card card) throws SQLException {
        String sql = "UPDATE bank_cards SET ownerUsername = ?, amount = ?, type = ?, registerDate = ?, isBlocked = ? WHERE id = ?";
        databaseService.executePreparedUpdate(sql,
                card.getOwnerUsername(),
                card.getAmount(),
                card.getType().name(),
                card.getRegisterDate().toString(),
                card.isBlocked() ? 1 : 0,
                card.getId());
        System.out.println("Bank card " + card.getId() + " updated successfully.");
    }

    // Method to retrieve all bank cards for a specific user
    public List<Card> getCardsByUser(String username) throws SQLException {
        List<Card> cards = new ArrayList<>();
        String sql = "SELECT * FROM bank_cards WHERE ownerUsername = ?";
        ResultSet resultSet = databaseService.executePreparedQuery(sql, username);

        while (resultSet.next()) {
            cards.add(new Card(
                    resultSet.getString("ownerUsername"),
                    resultSet.getDouble("amount"),
                    Card.CardType.valueOf(resultSet.getString("type"))
            ));
        }

        return cards;
    }
}
