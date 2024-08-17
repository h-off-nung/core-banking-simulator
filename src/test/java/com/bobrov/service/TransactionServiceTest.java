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

public class TransactionServiceTest {

    private DatabaseService databaseService;
    private UserService userService;
    private CardService bankCardService;
    private TransactionService transactionService;

    @Before
    public void setUp() throws SQLException {
        // Initialize the services
        databaseService = new DatabaseService();
        databaseService.connect();

        userService = new UserService(databaseService);
        bankCardService = new CardService(databaseService);
        transactionService = new TransactionService(databaseService);

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
    public void testCreateTransaction() throws SQLException {
        // Create a user and bank cards
        User user = new User("John", "Doe", LocalDate.of(1990, 1, 1), "Male", "johndoe", "password123");
        userService.createUser(user);

        Card senderCard = new Card("johndoe", 1000.0, Card.CardType.DEBIT);
        Card recipientCard = new Card("johndoe", 500.0, Card.CardType.CREDIT);
        bankCardService.createCard(senderCard);
        bankCardService.createCard(recipientCard);

        // Create a transaction
        Transaction transaction = new Transaction("123456", "654321", 150.0, Transaction.TransactionType.USER_TO_USER_SAME_BANK);
        transactionService.createTransaction(transaction);

        // Retrieve transactions for the sender card
        List<Transaction> transactions = transactionService.getTransactionsByCardId("123456");
        assertEquals(1, transactions.size());
        assertEquals("123456", transactions.get(0).getSenderCardId());
        assertEquals("654321", transactions.get(0).getRecipientCardId());
        assertEquals(150.0, transactions.get(0).getAmount(), 0.01);
    }

    @Test
    public void testGetTransactionsByCardId() throws SQLException {
        // Create a user and bank cards
        User user = new User("Jane", "Smith", LocalDate.of(1985, 5, 15), "Female", "janesmith", "securePass");
        userService.createUser(user);

        Card senderCard = new Card("janesmith", 1000.0, Card.CardType.DEBIT);
        Card recipientCard1 = new Card("janesmith", 500.0, Card.CardType.CREDIT);
        Card recipientCard2 = new Card("janesmith", 300.0, Card.CardType.DEBIT);
        bankCardService.createCard(senderCard);
        bankCardService.createCard(recipientCard1);
        bankCardService.createCard(recipientCard2);

        // Create transactions
        Transaction transaction1 = new Transaction("111111", "222222", 200.0, Transaction.TransactionType.USER_TO_USER_SAME_BANK);
        Transaction transaction2 = new Transaction("111111", "333333", 150.0, Transaction.TransactionType.USER_TO_USER_SAME_BANK);
        transactionService.createTransaction(transaction1);
        transactionService.createTransaction(transaction2);

        // Retrieve transactions for the sender card
        List<Transaction> transactions = transactionService.getTransactionsByCardId("111111");
        assertEquals(2, transactions.size());
        assertEquals("111111", transactions.get(0).getSenderCardId());
        assertEquals("222222", transactions.get(0).getRecipientCardId());
        assertEquals(200.0, transactions.get(0).getAmount(), 0.01);
        assertEquals("111111", transactions.get(1).getSenderCardId());
        assertEquals("333333", transactions.get(1).getRecipientCardId());
        assertEquals(150.0, transactions.get(1).getAmount(), 0.01);
    }

    @Test
    public void testGetAllTransactions() throws SQLException {
        // Create a user and bank cards
        User user = new User("Alex", "Johnson", LocalDate.of(2000, 8, 25), "Male", "alexjohnson", "myPassword");
        userService.createUser(user);

        Card senderCard = new Card("alexjohnson", 1500.0, Card.CardType.DEBIT);
        Card recipientCard = new Card("alexjohnson", 500.0, Card.CardType.CREDIT);
        bankCardService.createCard(senderCard);
        bankCardService.createCard(recipientCard);

        // Create transactions
        Transaction transaction1 = new Transaction("444444", "555555", 300.0, Transaction.TransactionType.USER_TO_USER_DIFFERENT_BANK);
        Transaction transaction2 = new Transaction("555555", "444444", 100.0, Transaction.TransactionType.USER_TO_USER_SAME_BANK);
        transactionService.createTransaction(transaction1);
        transactionService.createTransaction(transaction2);

        // Retrieve all transactions
        List<Transaction> transactions = transactionService.getAllTransactions();
        assertEquals(2, transactions.size());
        assertEquals("444444", transactions.get(0).getSenderCardId());
        assertEquals("555555", transactions.get(0).getRecipientCardId());
        assertEquals(300.0, transactions.get(0).getAmount(), 0.01);
        assertEquals("555555", transactions.get(1).getSenderCardId());
        assertEquals("444444", transactions.get(1).getRecipientCardId());
        assertEquals(100.0, transactions.get(1).getAmount(), 0.01);
    }
}
