package com.byteaid.appointment.service;

import com.byteaid.appointment.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    public User authenticate(String username, String password, String role) throws SQLException {
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pst = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?")) {
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                String actualRole = rs.getString("role");
                if (actualRole.equalsIgnoreCase(role)) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("gmail"));
                    user.setRole(rs.getString("role"));
                    return user;
                }
            }
            return null;
        }
    }

    public boolean registerUser(User user) throws SQLException {
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pst = conn.prepareStatement("INSERT INTO users(name, username, gmail, password, role) VALUES(?,?,?,?,?)")) {
            pst.setString(1, user.getName());
            pst.setString(2, user.getUsername());
            pst.setString(3, user.getEmail());
            pst.setString(4, user.getPassword());
            pst.setString(5, user.getRole());
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                return false;
            }
            throw e;
        }
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseService.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM users ORDER BY role, name")) {
            
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("gmail"));
                user.setRole(rs.getString("role"));
                users.add(user);
            }
        }
        return users;
    }

    public boolean deleteUser(int userId) throws SQLException {
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pst = conn.prepareStatement("DELETE FROM users WHERE id=?")) {
            pst.setInt(1, userId);
            return pst.executeUpdate() > 0;
        }
    }
    
    public List<User> getAdminUsers() throws SQLException {
        List<User> admins = new ArrayList<>();
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pst = conn.prepareStatement("SELECT * FROM users WHERE role='admin' ORDER BY name")) {
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("gmail"));
                user.setRole(rs.getString("role"));
                admins.add(user);
            }
        }
        return admins;
    }
    
    public boolean updateUser(int userId, String name, String username, String email, String role) throws SQLException {
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pst = conn.prepareStatement(
                 "UPDATE users SET name=?, username=?, gmail=?, role=? WHERE id=?")) {
            pst.setString(1, name);
            pst.setString(2, username);
            pst.setString(3, email);
            pst.setString(4, role);
            pst.setInt(5, userId);
            return pst.executeUpdate() > 0;
        }
    }
    
    public int getAdminCount() throws SQLException {
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pst = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE role = 'admin' OR role = 'Admin'")) {
            ResultSet rs = pst.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
}
