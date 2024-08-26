package com.bobrov.service;

import com.bobrov.model.Card;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CardService {

    private final DatabaseService databaseService;

    public CardService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public void createCard(Card card) throws SQLException {
        String sql = "INSERT INTO cards (id, ownerUsername, amount, type, registerDate, isBlocked) VALUES (?, ?, ?, ?, ?, ?)";
        databaseService.executePreparedUpdate(sql,
                card.getId(),
                card.getOwnerUsername(),
                card.getAmount(),
                card.getType().name(),
                card.getRegisterDate().toString(),
                card.isBlocked() ? 1 : 0);
        System.out.println("Bank card " + card.getId() + " created successfully.");
    }

    public Card getCardById(String cardId) throws SQLException {
        String sql = "SELECT * FROM cards WHERE id = ?";
        ResultSet resultSet = databaseService.executePreparedQuery(sql, cardId);

        if (resultSet.next()) {
            return new Card(
                    resultSet.getString("id"),
                    resultSet.getString("ownerUsername"),
                    resultSet.getDouble("amount"),
                    Card.CardType.valueOf(resultSet.getString("type"))
            );
        } else {
            return null;
        }
    }

    public void updateCard(Card card) throws SQLException {
        String sql = "UPDATE cards SET ownerUsername = ?, amount = ?, type = ?, registerDate = ?, isBlocked = ? WHERE id = ?";
        databaseService.executePreparedUpdate(sql,
                card.getOwnerUsername(),
                card.getAmount(),
                card.getType().name(),
                card.getRegisterDate().toString(),
                card.isBlocked() ? 1 : 0,
                card.getId());
        System.out.println("Bank card " + card.getId() + " updated successfully.");
    }

    public List<Card> getCardsByUser(String username) throws SQLException {
        List<Card> cards = new ArrayList<>();
        String sql = "SELECT * FROM cards WHERE ownerUsername = ?";
        ResultSet resultSet = databaseService.executePreparedQuery(sql, username);

        while (resultSet.next()) {
            cards.add(new Card(
                    resultSet.getString("id"),
                    resultSet.getString("ownerUsername"),
                    resultSet.getDouble("amount"),
                    Card.CardType.valueOf(resultSet.getString("type"))
            ));
        }

        return cards;
    }
}
