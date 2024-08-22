package com.bobrov.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseService {

    private static final String DB_URL = "jdbc:sqlite:bobrov_bank.db";
    private static final String ENCRYPTION_KEY = "r4lEPmYGYxrYXR$^mDQ2zF&Oo1&t6%"; // Replace with your actual encryption key

    private Connection connection;

    // Method to connect to the database with encryption
    public void connect() throws SQLException {
        try {
            // Load the SQLite JDBC driver (if not already loaded)
            Class.forName("org.sqlite.JDBC");
            // Set up the connection with encryption enabled
            this.connection = DriverManager.getConnection(DB_URL, "", ENCRYPTION_KEY);
            System.out.println("Connected to the database successfully.");
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC driver not found.", e);
        } catch (SQLException e) {
            throw new SQLException("Connection to the database failed.", e);
        }
    }

    // Method to execute a non-query SQL command (INSERT, UPDATE, DELETE)
    public void executeUpdate(String sql) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }

    // Method to execute a query SQL command (SELECT)
    public ResultSet executeQuery(String sql) throws SQLException {
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(sql);
    }

    // Method to execute a prepared statement with parameters (INSERT, UPDATE, DELETE)
    public void executePreparedUpdate(String sql, Object... params) throws SQLException {
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            pstmt.executeUpdate();
        }
    }

    // Method to execute a prepared query with parameters (SELECT)
    public ResultSet executePreparedQuery(String sql, Object... params) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
        return pstmt.executeQuery();
    }

    // Method to close the connection to the database
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to close the database connection.");
        }
    }
}
