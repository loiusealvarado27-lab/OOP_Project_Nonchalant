package com.byteaid.appointment.ui;

import com.byteaid.appointment.model.User;
import com.byteaid.appointment.service.UserService;
import com.byteaid.appointment.util.UIConstants;
import com.byteaid.appointment.util.UIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.sql.SQLException;

public class LoginPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;
    private UserService userService;

    public LoginPanel() {
        this.userService = new UserService();
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(UIConstants.BACKGROUND_COLOR);
        setBorder(new EmptyBorder(30, 30, 30, 30));
        
        JPanel card = UIUtils.createCard();
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        
        // Title
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("Welcome Back", JLabel.CENTER);
        titleLabel.setFont(UIConstants.HEADER_FONT);
        titleLabel.setForeground(UIConstants.PRIMARY_COLOR);
        card.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Username
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(UIConstants.LABEL_FONT);
        card.add(userLabel, gbc);
        gbc.gridx = 1;
        usernameField = UIUtils.createStyledTextField(20);
        card.add(usernameField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(UIConstants.LABEL_FONT);
        card.add(passLabel, gbc);
        gbc.gridx = 1;
        passwordField = UIUtils.createStyledPasswordField(20);
        card.add(passwordField, gbc);

        // Role
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel roleLabel = new JLabel("Login as:");
        roleLabel.setFont(UIConstants.LABEL_FONT);
        card.add(roleLabel, gbc);
        gbc.gridx = 1;
        roleCombo = new JComboBox<>(new String[]{"Customer", "Admin"});
        roleCombo.setFont(UIConstants.TEXT_FONT);
        card.add(roleCombo, gbc);

        // Login button
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(25, 15, 15, 15);
        JButton loginBtn = UIUtils.createStyledButton("Sign In", UIConstants.PRIMARY_COLOR);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginBtn.addActionListener(e -> login());
        card.add(loginBtn, gbc);
        
        add(card, BorderLayout.CENTER);
    }

    private void login() {
        String username = usernameField.getText();
        String password = String.valueOf(passwordField.getPassword());
        String role = (String) roleCombo.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password.");
            return;
        }

        try {
            User user = userService.authenticate(username, password, role);
            if (user != null) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                SwingUtilities.getWindowAncestor(this).dispose();
                
                if (user.getRole().equalsIgnoreCase("admin")) {
                    new AdminDashboard(user.getId());
                } else {
                    new UserDashboard(user.getId());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials or role mismatch.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
