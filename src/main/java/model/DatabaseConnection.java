package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/fms_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Default XAMPP password is empty
    
    private DatabaseConnection() {
        // Utility class - not meant to be instantiated
    }

    // Returns a NEW connection every time it's called.
    // Every DAO uses try-with-resources (try (Connection conn = ...)), which
    // automatically closes the connection at the end of each query. A cached
    // singleton connection would be closed after the very first query, and
    // every call after that would silently return a dead connection object,
    // causing "No operations allowed after connection closed" errors on the
    // second and all later database operations.
    public static Connection getConnection() {
        try {
            // Register JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully.");
            return connection;
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
            throw new RuntimeException("MySQL JDBC Driver not found. Did you reload the Maven project in IntelliJ?", e);
        } catch (SQLException e) {
            System.err.println("Connection to database failed. Make sure XAMPP MySQL is running.");
            e.printStackTrace();
            throw new RuntimeException("Could not connect to the database. Make sure XAMPP is running and credentials are correct.", e);
        }
    }
}
