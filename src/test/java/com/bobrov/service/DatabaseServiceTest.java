package com.bobrov.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class DatabaseServiceTest {

    private DatabaseService databaseService;

    @Before
    public void setUp() throws SQLException {
        // Initialize and connect to the database
        databaseService = new DatabaseService();
        databaseService.connect();

        // Set up a clean state before each test
        databaseService.executeUpdate("DROP TABLE IF EXISTS test_table");

        databaseService.executeUpdate(
                "CREATE TABLE test_table (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL)"
        );
    }

    @After
    public void tearDown() {
        // Close the database connection after each test
        databaseService.closeConnection();
    }

    @Test
    public void testConnect() {
        assertNotNull(databaseService);
        assertNotNull(databaseService); // Ensure the service is initialized
    }

    @Test
    public void testExecuteUpdate() throws SQLException {
        // Insert a record into the test_table
        String insertSql = "INSERT INTO test_table (name) VALUES ('TestName')";
        databaseService.executeUpdate(insertSql);

        // Verify the record was inserted
        String querySql = "SELECT * FROM test_table WHERE name = 'TestName'";
        ResultSet resultSet = databaseService.executeQuery(querySql);
        assertTrue(resultSet.next());
        assertEquals("TestName", resultSet.getString("name"));
    }

    @Test
    public void testExecuteQuery() throws SQLException {
        // Insert a record into the test_table
        String insertSql = "INSERT INTO test_table (name) VALUES ('QueryTest')";
        databaseService.executeUpdate(insertSql);

        // Execute a query to retrieve the record
        String querySql = "SELECT * FROM test_table WHERE name = 'QueryTest'";
        ResultSet resultSet = databaseService.executeQuery(querySql);

        // Verify the result set contains the correct data
        assertTrue(resultSet.next());
        assertEquals("QueryTest", resultSet.getString("name"));
        assertFalse(resultSet.next()); // Ensure there's only one record
    }

    @Test
    public void testExecutePreparedUpdate() throws SQLException {
        // Insert a record using a prepared statement
        String insertSql = "INSERT INTO test_table (name) VALUES (?)";
        databaseService.executePreparedUpdate(insertSql, "PreparedTest");

        // Verify the record was inserted
        String querySql = "SELECT * FROM test_table WHERE name = 'PreparedTest'";
        ResultSet resultSet = databaseService.executeQuery(querySql);
        assertTrue(resultSet.next());
        assertEquals("PreparedTest", resultSet.getString("name"));
    }

    @Test
    public void testExecutePreparedQuery() throws SQLException {
        // Insert a record into the test_table
        String insertSql = "INSERT INTO test_table (name) VALUES ('PreparedQueryTest')";
        databaseService.executeUpdate(insertSql);

        // Execute a prepared query to retrieve the record
        String querySql = "SELECT * FROM test_table WHERE name = ?";
        ResultSet resultSet = databaseService.executePreparedQuery(querySql, "PreparedQueryTest");

        // Verify the result set contains the correct data
        assertTrue(resultSet.next());
        assertEquals("PreparedQueryTest", resultSet.getString("name"));
        assertFalse(resultSet.next()); // Ensure there's only one record
    }

    @Test
    public void testCloseConnection() {
        // Close the connection
        databaseService.closeConnection();

        // Attempt to close again to ensure no exceptions occur
        try {
            databaseService.closeConnection();
        } catch (Exception e) {
            fail("Exception occurred while closing the connection: " + e.getMessage());
        }
    }
}

