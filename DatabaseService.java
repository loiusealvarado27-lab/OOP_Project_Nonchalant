package com.byteaid.appointment.service;

import java.sql.*;
import javax.swing.JOptionPane;

public class DatabaseService {
    private static final String DB_URL = "jdbc:sqlite:byteaid.db";

    public static void initializeDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
            createTables();
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "SQLite JDBC driver not found.", "Driver Missing", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    private static void createTables() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "username TEXT UNIQUE NOT NULL," +
                    "gmail TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL," +
                    "role TEXT NOT NULL)");

            stmt.execute("CREATE TABLE IF NOT EXISTS appointments (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_id INTEGER," +
                    "issue_type TEXT," +
                    "issue TEXT," +
                    "device TEXT," +
                    "description TEXT," +
                    "date TEXT," +
                    "time TEXT," +
                    "status TEXT DEFAULT 'Pending'," +
                    "assigned_technician_id TEXT," +
                    "FOREIGN KEY(user_id) REFERENCES users(id))");
            
            // Ensure assigned_technician_name column exists
            try {
                stmt.execute("ALTER TABLE appointments ADD COLUMN assigned_technician_name TEXT");
            } catch (SQLException e) {
                // Column might already exist, ignore
            }
            
            // Update any existing assignments to have proper technician names
            try {
                stmt.execute("UPDATE appointments SET assigned_technician_name = assigned_technician_id WHERE assigned_technician_id IS NOT NULL AND assigned_technician_name IS NULL");
            } catch (SQLException e) {
                // Ignore if update fails
            }
            
            // Create default admin accounts if they don't exist
            createDefaultAdminAccounts(stmt);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        }
    }
    
    private static void createDefaultAdminAccounts(Statement stmt) {
        String[] adminAccounts = {
            "INSERT OR IGNORE INTO users (name, username, gmail, password, role) VALUES ('Admin One', 'admin1', 'admin1@gmail.com', 'admin123', 'admin')",
            "INSERT OR IGNORE INTO users (name, username, gmail, password, role) VALUES ('Admin Two', 'admin2', 'admin2@gmail.com', 'admin123', 'admin')",
            "INSERT OR IGNORE INTO users (name, username, gmail, password, role) VALUES ('Admin Three', 'admin3', 'admin3@gmail.com', 'admin123', 'admin')",
            "INSERT OR IGNORE INTO users (name, username, gmail, password, role) VALUES ('Admin Four', 'admin4', 'admin4@gmail.com', 'admin123', 'admin')",
            "INSERT OR IGNORE INTO users (name, username, gmail, password, role) VALUES ('GG Admin', 'gg', 'gg@gmail.com', 'gg123', 'admin')"
        };
        
        for (String sql : adminAccounts) {
            try {
                stmt.execute(sql);
            } catch (SQLException e) {
                // Ignore if account already exists
            }
        }
    }
}
