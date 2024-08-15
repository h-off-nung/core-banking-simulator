package com.bobrov;

import com.bobrov.model.Admin;
import com.bobrov.model.User;
import com.bobrov.model.Card;
import com.bobrov.model.Transaction;
import com.bobrov.service.AdminService;
import com.bobrov.service.CardService;
import com.bobrov.service.DatabaseService;
import com.bobrov.service.TransactionService;
import com.bobrov.service.UserService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static DatabaseService databaseService;
    private static UserService userService;
    private static CardService bankCardService;
    private static TransactionService transactionService;
    private static AdminService adminService;

    public static void main(String[] args) {
        try {
            // Initialize services
            databaseService = new DatabaseService();
            databaseService.connect();

            userService = new UserService(databaseService);
            bankCardService = new CardService(databaseService);
            transactionService = new TransactionService(databaseService);
            adminService = new AdminService(userService, bankCardService, transactionService);

            // Start the application
            showMainMenu();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            databaseService.closeConnection();
        }
    }

    private static void showMainMenu() throws SQLException {
        while (true) {
            System.out.println("\n--- Bobrov Service ---");
            System.out.println("1. Log In");
            System.out.println("2. Sign Up");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    logIn();
                    break;
                case 2:
                    signUp();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void logIn() throws SQLException {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = userService.authenticateUser(username, password);
        if (user == null) {
            System.out.println("Invalid username or password.");
        } else if (user instanceof Admin) {
            showAdminMenu((Admin) user);
        } else {
            showUserMenu(user);
        }
    }

    private static void signUp() throws SQLException {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Surname: ");
        String surname = scanner.nextLine();
        System.out.print("Birthday (YYYY-MM-DD): ");
        LocalDate birthday = LocalDate.parse(scanner.nextLine());
        System.out.print("Sex: ");
        String sex = scanner.nextLine();
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        User newUser = User.signUp(name, surname, birthday, sex, username, password);
        userService.createUser(newUser);
        System.out.println("User " + username + " registered successfully.");
    }

    private static void showUserMenu(User user) throws SQLException {
        while (true) {
            System.out.println("\n--- User Menu ---");
            System.out.println("1. View My Cards");
            System.out.println("2. Add Card");
            System.out.println("3. View Transactions");
            System.out.println("4. Perform Transaction");
            System.out.println("5. View Statistics");
            System.out.println("6. Log Out");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    viewUserCards(user);
                    break;
                case 2:
                    addUserCard(user);
                    break;
                case 3:
                    viewUserTransactions(user);
                    break;
                case 4:
                    performTransaction(user);
                    break;
                case 5:
                    viewUserStatistics(user);
                    break;
                case 6:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void showAdminMenu(Admin admin) throws SQLException {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. View All Users");
            System.out.println("2. View All Transactions");
            System.out.println("3. Block Card");
            System.out.println("4. Unblock Card");
            System.out.println("5. Log Out");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    adminService.viewAllUsers();
                    break;
                case 2:
                    adminService.viewAllTransactions();
                    break;
                case 3:
                    blockCard();
                    break;
                case 4:
                    unblockCard();
                    break;
                case 5:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void viewUserCards(User user) throws SQLException {
        List<Card> cards = bankCardService.getCardsByUser(user.getUsername());
        if (cards.isEmpty()) {
            System.out.println("You have no cards.");
        } else {
            cards.forEach(card -> {
                System.out.println("Card ID: " + card.getId() + ", Balance: " + card.getAmount() + ", Type: " + card.getType());
            });
        }
    }

    private static void addUserCard(User user) throws SQLException {
        if (user.getCards().size() >= 3) {
            System.out.println("You can't add more than 3 cards.");
            return;
        }

        String cardId = Card.generateCardId();
        System.out.print("Initial Balance: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        System.out.print("Card Type (CREDIT/DEBIT): ");
        Card.CardType type = Card.CardType.valueOf(scanner.nextLine().toUpperCase());

        Card newCard = new Card(user.getUsername(), amount, type);
        bankCardService.createCard(newCard);
        user.addCard(newCard);
        System.out.println("Card " + cardId + " added successfully.");
    }

    private static void viewUserTransactions(User user) throws SQLException {
        List<Card> cards = bankCardService.getCardsByUser(user.getUsername());
        for (Card card : cards) {
            List<Transaction> transactions = transactionService.getTransactionsByCardId(card.getId());
            if (transactions.isEmpty()) {
                System.out.println("No transactions found for card " + card.getId());
            } else {
                transactions.forEach(transaction -> {
                    System.out.println(transaction.logTransaction());
                });
            }
        }
    }

    private static void performTransaction(User user) throws SQLException {
        System.out.print("Sender Card ID: ");
        String senderCardId = scanner.nextLine();
        System.out.print("Recipient Card ID: ");
        String recipientCardId = scanner.nextLine();
        System.out.print("Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        System.out.print("Transaction Type (USER_TO_USER_SAME_BANK/USER_TO_USER_DIFFERENT_BANK/WITHDRAWAL): ");
        Transaction.TransactionType type = Transaction.TransactionType.valueOf(scanner.nextLine().toUpperCase());

        Transaction newTransaction = new Transaction(senderCardId, recipientCardId, amount, type);
        transactionService.createTransaction(newTransaction);
        System.out.println("Transaction performed successfully.");
    }

    private static void viewUserStatistics(User user) {
        // Implement the logic to view statistics, like amount and transactions number of a month
        System.out.println("Statistics feature is not implemented yet.");
    }

    private static void blockCard() throws SQLException {
        System.out.print("Enter Card ID to block: ");
        String cardId = scanner.nextLine();
        adminService.blockCard(cardId);
    }

    private static void unblockCard() throws SQLException {
        System.out.print("Enter Card ID to unblock: ");
        String cardId = scanner.nextLine();
        adminService.unblockCard(cardId);
    }
}
