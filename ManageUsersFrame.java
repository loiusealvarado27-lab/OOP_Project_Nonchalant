package com.byteaid.appointment.ui;

import com.byteaid.appointment.model.User;
import com.byteaid.appointment.service.UserService;
import com.byteaid.appointment.util.UIConstants;
import com.byteaid.appointment.util.UIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ManageUsersFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private final UserService userService;

    public ManageUsersFrame() {
        this.userService = new UserService();
        initializeComponents();
    }

    private void initializeComponents() {
        setTitle("User Management");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(UIConstants.BACKGROUND_COLOR);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JLabel titleLabel = new JLabel("User Management", JLabel.CENTER);
        titleLabel.setFont(UIConstants.HEADER_FONT);
        titleLabel.setForeground(UIConstants.PRIMARY_COLOR);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Name", "Username", "Email", "Role"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setFont(UIConstants.TEXT_FONT);
        table.setRowHeight(25);
        table.getTableHeader().setFont(UIConstants.LABEL_FONT);
        table.getTableHeader().setBackground(UIConstants.PRIMARY_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        
        loadUsers();
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(new Color(189, 195, 199), 1));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        JButton editBtn = UIUtils.createStyledButton("Edit User", UIConstants.PRIMARY_COLOR);
        editBtn.addActionListener(e -> editUser());
        
        JButton deleteBtn = UIUtils.createStyledButton("Delete User", UIConstants.DANGER_COLOR);
        deleteBtn.addActionListener(e -> deleteUser());
        
        JButton refreshBtn = UIUtils.createStyledButton("Refresh", UIConstants.SECONDARY_COLOR);
        refreshBtn.addActionListener(e -> loadUsers());
        
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
        setVisible(true);
    }

    private void loadUsers() {
        model.setRowCount(0);
        try {
            List<User> users = userService.getAllUsers();
            for (User user : users) {
                Object[] row = {
                    user.getId(),
                    user.getName(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole()
                };
                model.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading users: " + ex.getMessage());
        }
    }

    private void editUser() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int userId = (Integer) model.getValueAt(selectedRow, 0);
        String currentName = (String) model.getValueAt(selectedRow, 1);
        String currentUsername = (String) model.getValueAt(selectedRow, 2);
        String currentEmail = (String) model.getValueAt(selectedRow, 3);
        String currentRole = (String) model.getValueAt(selectedRow, 4);
        
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JTextField nameField = new JTextField(currentName);
        JTextField usernameField = new JTextField(currentUsername);
        JTextField emailField = new JTextField(currentEmail);
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"customer", "admin"});
        roleCombo.setSelectedItem(currentRole);
        
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Role:"));
        panel.add(roleCombo);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Edit User Details", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String newName = nameField.getText().trim();
            String newUsername = usernameField.getText().trim();
            String newEmail = emailField.getText().trim();
            String newRole = (String) roleCombo.getSelectedItem();
            
            if (newName.isEmpty() || newUsername.isEmpty() || newEmail.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                if (userService.updateUser(userId, newName, newUsername, newEmail, newRole)) {
                    JOptionPane.showMessageDialog(this, "User updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadUsers();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update user. Username might already exist.", "Update Failed", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error updating user: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void deleteUser() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int userId = (Integer) model.getValueAt(selectedRow, 0);
        String userName = (String) model.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete user '" + userName + "'?\nThis action cannot be undone.", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (userService.deleteUser(userId)) {
                    JOptionPane.showMessageDialog(this, "User deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadUsers();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete user.", "Delete Failed", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting user: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
