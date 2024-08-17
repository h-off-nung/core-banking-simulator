package com.bobrov.service;

import com.bobrov.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class UserServiceTest {

    private DatabaseService databaseService;
    private UserService userService;

    @Before
    public void setUp() throws SQLException {
        // Initialize the database service and user service
        databaseService = new DatabaseService();
        databaseService.connect();

        userService = new UserService(databaseService);

        // Set up a clean state before each test
        databaseService.executeUpdate("DROP TABLE IF EXISTS users");
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
    }

    @After
    public void tearDown() {
        // Close the database connection after each test
        databaseService.closeConnection();
    }

    @Test
    public void testCreateUser() throws SQLException {
        User user = new User("John", "Doe", LocalDate.of(1990, 1, 1), "Male", "johndoe", "password123");
        userService.createUser(user);

        User retrievedUser = userService.getUserByUsername("johndoe");

        assertNotNull(retrievedUser);
        assertEquals("John", retrievedUser.getName());
        assertEquals("Doe", retrievedUser.getSurname());
        assertEquals("1990-01-01", retrievedUser.getBirthday().toString());
        assertEquals("Male", retrievedUser.getSex());
        assertEquals("johndoe", retrievedUser.getUsername());
        assertEquals("password123", retrievedUser.getPassword());
    }

    @Test
    public void testAuthenticateUser() throws SQLException {
        User user = new User("Jane", "Smith", LocalDate.of(1985, 5, 15), "Female", "janesmith", "securePass");
        userService.createUser(user);

        User authenticatedUser = userService.authenticateUser("janesmith", "securePass");

        assertNotNull(authenticatedUser);
        assertEquals("Jane", authenticatedUser.getName());
        assertEquals("Smith", authenticatedUser.getSurname());
        assertEquals("1985-05-15", authenticatedUser.getBirthday().toString());
        assertEquals("Female", authenticatedUser.getSex());
        assertEquals("janesmith", authenticatedUser.getUsername());
    }

    @Test
    public void testAuthenticateUserWithInvalidCredentials() throws SQLException {
        User user = new User("Alex", "Johnson", LocalDate.of(2000, 8, 25), "Male", "alexjohnson", "myPassword");
        userService.createUser(user);

        User authenticatedUser = userService.authenticateUser("alexjohnson", "wrongPassword");

        assertNull(authenticatedUser);
    }

    @Test
    public void testGetAllUsers() throws SQLException {
        User user1 = new User("John", "Doe", LocalDate.of(1990, 1, 1), "Male", "johndoe", "password123");
        User user2 = new User("Jane", "Smith", LocalDate.of(1985, 5, 15), "Female", "janesmith", "securePass");

        userService.createUser(user1);
        userService.createUser(user2);

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
        assertEquals("John", users.get(0).getName());
        assertEquals("Jane", users.get(1).getName());
    }

    @Test
    public void testUpdateUser() throws SQLException {
        User user = new User("Emily", "Clark", LocalDate.of(1992, 7, 12), "Female", "emilyclark", "initialPass");
        userService.createUser(user);

        // Update user details
        user.setName("Emma");
        user.setPassword("newSecurePass");
        userService.updateUser(user);

        User updatedUser = userService.getUserByUsername("emilyclark");

        assertNotNull(updatedUser);
        assertEquals("Emma", updatedUser.getName());
        assertEquals("newSecurePass", updatedUser.getPassword());
    }
}
