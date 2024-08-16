## 🏦 The Core Banking Simulator

Welcome to **The Core Banking Simulator**, a console-based banking application built with Java and SQLite. This project showcases a complete banking system with user authentication, card management, and transaction processing.

[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com)
[![SQLite](https://img.shields.io/badge/SQLite-07405E?style=for-the-badge&logo=sqlite&logoColor=white)](https://www.sqlite.org)
[![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)](https://maven.apache.org)
### ✨ Features

- **User Authentication**: Users can sign up and log in using their unique credentials.
- **Admin Controls**: Admins have full control over user accounts and can block/unblock bank cards.
- **Card Management**: Users can register up to 3 bank cards, view card details, and manage balances.
- **Transactions**: Users can perform transactions, including withdrawals and transfers, with a daily limit.
- **Transaction History**: Users can view their transaction history and statistics.
- **SQLite Database**: All data is securely stored in an encrypted SQLite database.

### 🛠 Configuration
The application is configured via the application.properties file located in src/main/resources. Here you can set your database URL, encryption key, and other settings.

Make sure to replace the existing encryption key with your own.

### 📝 Usage
**User Operations:**
- **Sign Up:** Register a new user with details such as name, birthday, and password.
- **Log In:** Access your account using your username and password. 
- **View Cards:** Display details of your registered bank cards. 
- **Add Card:** Register a new bank card (up to 3 per user). 
- **Perform Transactions:** Transfer funds between users or withdraw money from an ATM. 

**Admin Operations:**
- **View All Users:** Display a list of all registered users. 
- **View All Transactions:** Display all transactions in the system. 
- **Block/Unblock Cards:** Restrict or restore access to user bank cards.
### 🔒 Security
**Encrypted Database:** The application uses SQLCipher to encrypt the SQLite database, ensuring that all sensitive data is secure.
### 📊 Statistics (idea)
Users can view monthly statistics for their transactions, including total transaction amounts and the number of transactions.