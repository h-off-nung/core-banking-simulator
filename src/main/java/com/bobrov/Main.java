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
    private static CardService CardService;
    private static TransactionService transactionService;
    private static AdminService adminService;

    public static void main(String[] args) {
        try {
            // Initialize services
            databaseService = new DatabaseService();
            databaseService.connect();
            databaseService.createTables();
            userService = new UserService(databaseService);
            CardService = new CardService(databaseService);
            transactionService = new TransactionService(databaseService);
            adminService = new AdminService(databaseService, userService, CardService, transactionService);
            // Create an admin
//            try {
//                Admin newAdmin = new Admin("Marques", "Brownlee", LocalDate.of(1993, 12, 3), "m", "mkbhd", "quality tech videos");
//                adminService.createAdmin(newAdmin);
//            } catch (SQLException e) {
//                System.out.println("Error creating admin: " + e.getMessage());
//            }
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
            System.out.println("\n--- Bobrov Bank ---");
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

        if (adminService.isPersonAdmin(username, password) == 1) {
            showAdminMenu(adminService.authenticateAdmin(username, password));
        } else if (adminService.isPersonAdmin(username, password) == 0) {
            showUserMenu(userService.authenticateUser(username, password));
        } else {
            System.out.println("Invalid username or password.");
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
        List<Card> cards = CardService.getCardsByUser(user.getUsername());
        if (cards.isEmpty()) {
            System.out.println("You have no cards.");
        } else {
            cards.forEach(card -> {
                System.out.println("Card ID: " + card.getId() + ", Balance: " + card.getAmount() + ", Type: " + card.getType());
            });
        }
    }

    private static void addUserCard(User user) throws SQLException {
        if (CardService.getCardsByUser(user.getUsername()).size() >= 3) {
            System.out.println("You can't add more than 3 cards.");
            return;
        }
        System.out.print("Initial Balance: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        System.out.print("Card Type (1 - CREDIT, 2 - DEBIT): ");
        int typeChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        Card.CardType type;
        if (typeChoice == 1) {
            type = Card.CardType.CREDIT;
        } else if (typeChoice == 2) {
            type = Card.CardType.DEBIT;
        } else {
            System.out.println("Invalid card type choice.");
            return;
        }

        Card newCard = new Card(user.getUsername(), amount, type);
        CardService.createCard(newCard);
        System.out.println("Card " + newCard.getId() + " added successfully.");
    }

    private static void viewUserTransactions(User user) throws SQLException {
        List<Card> cards = CardService.getCardsByUser(user.getUsername());
        if (cards.isEmpty()) {
            System.out.println("You have no cards.");
            return;
        }
        System.out.println("Choose a card to view transactions:");
        for (int i = 0; i < cards.size(); i++) {
            System.out.println(i + 1 + ". Card ID: " + cards.get(i).getId());
        }
        int choice = scanner.nextInt();
        if (choice < 1 || choice > cards.size()) {
            System.out.println("Invalid choice.");
            return;
        }
        Card card = cards.get(choice - 1);
        List<Transaction> transactions = transactionService.getTransactionsByCardId(card.getId());
        if (transactions.isEmpty()) {
            System.out.println("No transactions found for card " + card.getId());
        } else {
            transactions.forEach(transaction -> {
                System.out.println(transaction.logTransaction());
            });
        }
    }


    private static void performTransaction(User user) throws SQLException {
        List<Card> userCards = CardService.getCardsByUser(user.getUsername());
        if (userCards.isEmpty()) {
            System.out.println("No cards found for user " + user.getUsername());
            return;
        }
        System.out.println("Choose a card to perform a transaction:");
        for (int i = 0; i < userCards.size(); i++) {
            System.out.println(i + 1 + ". Card ID: " + userCards.get(i).getId() + ", Balance: " + userCards.get(i).getAmount() + ", Type: " + userCards.get(i).getType());
        }
        int choice = scanner.nextInt();
        if (choice < 1 || choice > userCards.size()) {
            System.out.println("Invalid choice.");
            return;
        }
        Card senderCard = userCards.get(choice - 1);
        System.out.print("Recipient Card ID: ");
        String recipientCardId = scanner.next();
        System.out.print("Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        if (senderCard.getAmount() < amount) {
            System.out.println("Insufficient balance.");
            return;
        }
        System.out.print("Enter transaction type number (1 - User to user same bank, 2 - User to user different bank, 3 - Withdrawal): ");
        int typeChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        Transaction.TransactionType type;
        if (typeChoice == 1) {
            type = Transaction.TransactionType.USER_TO_USER_SAME_BANK;
        } else if (typeChoice == 2) {
            type = Transaction.TransactionType.USER_TO_USER_DIFFERENT_BANK;
        } else if (typeChoice == 3) {
            type = Transaction.TransactionType.WITHDRAWAL;
        } else {
            System.out.println("Invalid transaction type choice.");
            return;
        }
        if (type == Transaction.TransactionType.USER_TO_USER_SAME_BANK) {
            Card recipientCard = CardService.getCardById(recipientCardId);
            if (recipientCard == null) {
                System.out.println("Recipient card not found.");
                return;
            }
            transactionService.performTransaction(senderCard, recipientCard, recipientCardId, amount, type, CardService);
        } else {
            transactionService.performTransaction(senderCard, null, recipientCardId, amount, type, CardService);
        }
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
