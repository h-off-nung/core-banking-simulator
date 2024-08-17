package com.bobrov.service;

import com.bobrov.model.Card;
import com.bobrov.model.Transaction;
import com.bobrov.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class AdminServiceTest {

    private DatabaseService databaseService;
    private UserService userService;
    private CardService bankCardService;
    private TransactionService transactionService;
    private AdminService adminService;

    @Before
    public void setUp() throws SQLException {
        // Initialize the services
        databaseService = new DatabaseService();
        databaseService.connect();

        userService = new UserService(databaseService);
        bankCardService = new CardService(databaseService);
        transactionService = new TransactionService(databaseService);
        adminService = new AdminService(userService, bankCardService, transactionService);

        // Set up a clean state before each test
        databaseService.executeUpdate("DROP TABLE IF EXISTS users");
        databaseService.executeUpdate("DROP TABLE IF EXISTS bank_cards");
        databaseService.executeUpdate("DROP TABLE IF EXISTS transactions");

        databaseService.executeUpdate(
                "CREATE TABLE users (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "surname TEXT NOT NULL, " +
                        "birthday TEXT NOT NULL, " +
                        "sex TEXT NOT NULL, " +
                        "username TEXT UNIQUE NOT NULL, " +
                        "password TEXT NOT NULL)"
        );

        databaseService.executeUpdate(
                "CREATE TABLE bank_cards (" +
                        "id TEXT PRIMARY KEY, " +
                        "ownerUsername TEXT NOT NULL, " +
                        "amount REAL NOT NULL, " +
                        "type TEXT NOT NULL, " +
                        "registerDate TEXT NOT NULL, " +
                        "isBlocked INTEGER NOT NULL, " +
                        "FOREIGN KEY(ownerUsername) REFERENCES users(username))"
        );

        databaseService.executeUpdate(
                "CREATE TABLE transactions (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "senderCardId TEXT NOT NULL, " +
                        "recipientCardId TEXT NOT NULL, " +
                        "dateTime TEXT NOT NULL, " +
                        "amount REAL NOT NULL, " +
                        "type TEXT NOT NULL, " +
                        "FOREIGN KEY(senderCardId) REFERENCES bank_cards(id), " +
                        "FOREIGN KEY(recipientCardId) REFERENCES bank_cards(id))"
        );
    }

    @After
    public void tearDown() {
        // Close the database connection after each test
        databaseService.closeConnection();
    }

    @Test
    public void testBlockCard() throws SQLException {
        // Create a user and a bank card
        User user = new User("John", "Doe", LocalDate.of(1990, 1, 1), "Male", "johndoe", "password123");
        userService.createUser(user);

        Card card = new Card("johndoe", 1000.0, Card.CardType.DEBIT);
        bankCardService.createCard(card);

        // Block the card
        adminService.blockCard("123456");

        // Retrieve the card and check its status
        Card blockedCard = bankCardService.getCardById("123456");
        assertNotNull(blockedCard);
        assertTrue(blockedCard.isBlocked());
    }

    @Test
    public void testUnblockCard() throws SQLException {
        // Create a user and a blocked bank card
        User user = new User("Jane", "Smith", LocalDate.of(1985, 5, 15), "Female", "janesmith", "securePass");
        userService.createUser(user);

        Card card = new Card("janesmith", 2000.0, Card.CardType.CREDIT);
        card.block(); // Block the card initially
        bankCardService.createCard(card);

        // Unblock the card
        adminService.unblockCard("654321");

        // Retrieve the card and check its status
        Card unblockedCard = bankCardService.getCardById("654321");
        assertNotNull(unblockedCard);
        assertFalse(unblockedCard.isBlocked());
    }

    @Test
    public void testViewAllUsers() throws SQLException {
        // Create multiple users
        User user1 = new User("John", "Doe", LocalDate.of(1990, 1, 1), "Male", "johndoe", "password123");
        User user2 = new User("Jane", "Smith", LocalDate.of(1985, 5, 15), "Female", "janesmith", "securePass");

        userService.createUser(user1);
        userService.createUser(user2);

        // Capture the output to validate the list of users
        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
        assertEquals("John", users.get(0).getName());
        assertEquals("Jane", users.get(1).getName());
    }

    @Test
    public void testViewAllTransactions() throws SQLException {
        // Create a user, bank cards, and transactions
        User user = new User("Alex", "Johnson", LocalDate.of(2000, 8, 25), "Male", "alexjohnson", "myPassword");
        userService.createUser(user);

        Card card1 = new Card("alexjohnson", 1500.0, Card.CardType.DEBIT);
        Card card2 = new Card("alexjohnson", 500.0, Card.CardType.CREDIT);
        bankCardService.createCard(card1);
        bankCardService.createCard(card2);

        Transaction transaction1 = new Transaction("111111", "222222", 100.0, Transaction.TransactionType.USER_TO_USER_SAME_BANK);
        Transaction transaction2 = new Transaction("222222", "111111", 50.0, Transaction.TransactionType.USER_TO_USER_DIFFERENT_BANK);

        transactionService.createTransaction(transaction1);
        transactionService.createTransaction(transaction2);

        // Capture the output to validate the list of transactions
        List<Transaction> transactions = transactionService.getAllTransactions();

        assertEquals(2, transactions.size());
        assertEquals("111111", transactions.get(0).getSenderCardId());
        assertEquals("222222", transactions.get(1).getSenderCardId());
    }
}
