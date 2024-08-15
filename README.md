# 📜 The Core Banking Simulator

Welcome to the **Core Banking Simulator** project! This application is built using **Java** with an **SQLite** encrypted database, structured with **Maven**, and developed in **IntelliJ IDEA**. The application is designed for console-based control without a graphical user interface (GUI), ensuring a straightforward and secure experience for managing user and admin functionalities.

## 🚀 Features

### 🛠️ Project Setup
- **Java**: Core language used for application logic.
- **SQLite**: Secure, encrypted database for storing sensitive information.
- **Maven**: Project structure and dependency management.
- **IntelliJ IDEA**: Integrated Development Environment (IDE) for development.

### 🔒 Security
- **Encrypted Database**: All data is stored in an encrypted SQLite database.
- **User Roles**: Different rights for **Admin** and **User**.
- **Authentication**: Secure log in with username and password.

### 🧑‍💼 Admin Capabilities
- **Full Database Access**: Admins have unrestricted access to the entire database.
- **Card Management**: Admins can block or unblock user cards.

### 👥 User Capabilities
- **User Attributes**: Name, surname, birthday, sex, username, password, and registered cards.
- **Transaction Management**: Users can view and perform transactions with a daily limit.
- **Card Management**: Users can register up to 3 bank cards.
- **Statistics**: Users can view monthly transaction statistics (total amount and number of transactions).

### 💳 Bank Cards
- **Attributes**: Each card has a unique 6-digit ID, owner's username, balance, type (credit or debit), registration date, and a blocking status.
- **Card Types**: Credit and debit cards.
- **Card Limits**: Maximum of 3 cards per user.

### 💸 Transactions
- **Attributes**: Sender and recipient card IDs, date, time, and amount.
- **Types**:
    - **User to User (Same Bank)**: Internal transfers between users of the same bank.
    - **User to User (Different Bank)**: Transfers between users of different banks.
    - **Withdrawals**: Cash withdrawals where the recipient field stores the ATM's ID.

## 🏗️ Project Structure

### 1. **Main.java**
- **Purpose**: The entry point of the application.
- **Functions**: Handles the console interface, manages user authentication, and navigates the start menu.

### 2. **Database Package**
- **DatabaseManager.java**: Manages all CRUD (Create, Read, Update, Delete) operations in the database.
- **EncryptionUtil.java**: Ensures all database interactions are securely encrypted and decrypted.

### 3. **Model Package**
- **User.java**: Defines a user with all necessary attributes like name, surname, etc.
- **Admin.java**: Inherits from or implements User, with added administrative privileges.
- **BankCard.java**: Represents a bank card, including details like card type, ID, and owner.
- **Transaction.java**: Represents a transaction, capturing details like sender, recipient, and amount.

### 4. **Service Package**
- **UserService.java**: Handles all user-related operations, including registration, login, and transaction viewing.
- **AdminService.java**: Manages admin-specific tasks such as blocking or unblocking user cards.
- **TransactionService.java**: Facilitates transaction-related operations, ensuring proper logging and management.
- **CardService.java**: Oversees card-related actions, including registration and viewing of cards.

### 5. **Util Package**
- **InputValidator.java**: Validates all user inputs to maintain data integrity and security.