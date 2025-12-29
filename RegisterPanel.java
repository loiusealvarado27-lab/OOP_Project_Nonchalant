package com.byteaid.appointment.ui;

import com.byteaid.appointment.model.User;
import com.byteaid.appointment.service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

public class RegisterPanel extends JPanel {
    private JTextField nameField, usernameField, gmailField;
    private JPasswordField passwordField, confirmPasswordField;
    private JToggleButton showPasswordBtn, showConfirmPasswordBtn;
    private JComboBox<String> roleCombo;
    private final UserService userService;
    private final AuthFrame parentFrame;

    public RegisterPanel(AuthFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.userService = new UserService();
        initializeComponents();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));
        
        JPanel centerContainer = new JPanel(new GridBagLayout());
        centerContainer.setBackground(new Color(245, 247, 250));
        
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230), 1),
            new EmptyBorder(10, 35, 20, 35)));
        card.setPreferredSize(new Dimension(420, 580));
        
        // Header
        card.add(createHeader());
        card.add(Box.createVerticalStrut(5));
        
        // Form
        card.add(createForm());
        card.add(Box.createVerticalStrut(10));
        
        // Buttons
        card.add(createButtons());
        
        centerContainer.add(card);
        add(centerContainer, BorderLayout.CENTER);
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(Color.WHITE);
        
        JLabel logoLabel = new JLabel("🔧", JLabel.CENTER);
        logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        logoLabel.setForeground(new Color(0, 123, 255));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel brandLabel = new JLabel("ByteAid", JLabel.CENTER);
        brandLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        brandLabel.setForeground(new Color(33, 37, 41));
        brandLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel taglineLabel = new JLabel("Device Repair & Support", JLabel.CENTER);
        taglineLabel.setFont(new Font("Open Sans", Font.PLAIN, 12));
        taglineLabel.setForeground(new Color(108, 117, 125));
        taglineLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        header.add(logoLabel);
        header.add(Box.createVerticalStrut(2));
        header.add(brandLabel);
        header.add(taglineLabel);
        
        return header;
    }
    
    private JPanel createForm() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);
        
        // Full Name
        form.add(createFieldLabel("Full Name"));
        form.add(Box.createVerticalStrut(3));
        nameField = createTextField();
        form.add(nameField);
        form.add(Box.createVerticalStrut(10));
        
        // Username
        form.add(createFieldLabel("Username"));
        form.add(Box.createVerticalStrut(3));
        usernameField = createTextField();
        form.add(usernameField);
        form.add(Box.createVerticalStrut(10));
        
        // Gmail Address
        form.add(createFieldLabel("Gmail Address"));
        form.add(Box.createVerticalStrut(3));
        gmailField = createTextField();
        form.add(gmailField);
        form.add(Box.createVerticalStrut(10));
        
        // Password
        form.add(createFieldLabel("Password"));
        form.add(Box.createVerticalStrut(3));
        form.add(createPasswordPanel());
        form.add(Box.createVerticalStrut(10));
        
        // Confirm Password
        form.add(createFieldLabel("Confirm Password"));
        form.add(Box.createVerticalStrut(3));
        form.add(createConfirmPasswordPanel());
        form.add(Box.createVerticalStrut(10));
        
        // Role
        form.add(createFieldLabel("Role"));
        form.add(Box.createVerticalStrut(3));
        roleCombo = createRoleCombo();
        form.add(roleCombo);
        
        return form;
    }
    
    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Roboto", Font.BOLD, 14));
        label.setForeground(new Color(52, 58, 64));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
    
    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Open Sans", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(320, 35));
        field.setMaximumSize(new Dimension(320, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        return field;
    }
    
    private JPanel createPasswordPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(320, 35));
        
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Open Sans", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 30)));
        passwordField.setEchoChar('•');
        
        showPasswordBtn = new JToggleButton("👁");
        showPasswordBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        showPasswordBtn.setPreferredSize(new Dimension(30, 35));
        showPasswordBtn.setBorder(BorderFactory.createEmptyBorder());
        showPasswordBtn.setBackground(Color.WHITE);
        showPasswordBtn.setFocusPainted(false);
        showPasswordBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        showPasswordBtn.addActionListener(e -> togglePasswordVisibility(passwordField, showPasswordBtn));
        
        panel.add(passwordField, BorderLayout.CENTER);
        panel.add(showPasswordBtn, BorderLayout.EAST);
        return panel;
    }
    
    private JPanel createConfirmPasswordPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(320, 35));
        
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setFont(new Font("Open Sans", Font.PLAIN, 14));
        confirmPasswordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 30)));
        confirmPasswordField.setEchoChar('•');
        
        showConfirmPasswordBtn = new JToggleButton("👁");
        showConfirmPasswordBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        showConfirmPasswordBtn.setPreferredSize(new Dimension(30, 35));
        showConfirmPasswordBtn.setBorder(BorderFactory.createEmptyBorder());
        showConfirmPasswordBtn.setBackground(Color.WHITE);
        showConfirmPasswordBtn.setFocusPainted(false);
        showConfirmPasswordBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        showConfirmPasswordBtn.addActionListener(e -> togglePasswordVisibility(confirmPasswordField, showConfirmPasswordBtn));
        
        panel.add(confirmPasswordField, BorderLayout.CENTER);
        panel.add(showConfirmPasswordBtn, BorderLayout.EAST);
        return panel;
    }
    
    private JComboBox<String> createRoleCombo() {
        String[] roles = {"Customer", "Admin"};
        JComboBox<String> combo = new JComboBox<>(roles);
        combo.setSelectedIndex(0);
        combo.setFont(new Font("Open Sans", Font.PLAIN, 14));
        combo.setPreferredSize(new Dimension(320, 35));
        combo.setMaximumSize(new Dimension(320, 35));
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createLineBorder(new Color(206, 212, 218), 1));
        return combo;
    }
    

    
    private JPanel createButtons() {
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
        buttons.setBackground(Color.WHITE);
        
        // Create horizontal panel for side-by-side buttons
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonRow.setBackground(Color.WHITE);
        
        // Back to Login button
        JButton backBtn = new JButton("Back to Login");
        backBtn.setFont(new Font("Roboto", Font.BOLD, 12));
        backBtn.setPreferredSize(new Dimension(150, 40));
        backBtn.setBackground(Color.WHITE);
        backBtn.setForeground(new Color(0, 123, 255));
        backBtn.setBorder(BorderFactory.createLineBorder(new Color(0, 123, 255), 1));
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> parentFrame.showSignIn());
        
        // Register button
        JButton registerBtn = new JButton("Register");
        registerBtn.setFont(new Font("Roboto", Font.BOLD, 14));
        registerBtn.setPreferredSize(new Dimension(160, 40));
        registerBtn.setBackground(new Color(0, 123, 255));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setBorder(BorderFactory.createEmptyBorder());
        registerBtn.setFocusPainted(false);
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerBtn.addActionListener(e -> register());
        
        buttonRow.add(backBtn);
        buttonRow.add(registerBtn);
        
        buttons.add(buttonRow);
        
        return buttons;
    }
    
    private void togglePasswordVisibility(JPasswordField field, JToggleButton button) {
        if (button.isSelected()) {
            field.setEchoChar((char) 0);
            button.setText("🙈");
        } else {
            field.setEchoChar('•');
            button.setText("👁");
        }
    }
    
    private void register() {
        String name = nameField.getText().trim();
        String username = usernameField.getText().trim();
        String gmail = gmailField.getText().trim();
        String password = String.valueOf(passwordField.getPassword());
        String confirmPassword = String.valueOf(confirmPasswordField.getPassword());
        String role = (String) roleCombo.getSelectedItem();

        if (name.isEmpty() || username.isEmpty() || gmail.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Incomplete Form", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match. Please try again.", "Password Mismatch", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters long.", "Weak Password", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!gmail.toLowerCase().contains("@gmail.com")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Gmail address.", "Invalid Email", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Check admin limit before registration
            if ("Admin".equalsIgnoreCase(role) && userService.getAdminCount() >= 5) {
                JOptionPane.showMessageDialog(this, "Maximum number of admin accounts (5) has been reached.\nPlease register as a Customer instead.", "Admin Limit Reached", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            User user = new User(name, username, gmail, password, role);
            if (userService.registerUser(user)) {
                JOptionPane.showMessageDialog(this, "Account created successfully!\nWelcome to ByteAid, " + name + "!\nYou can now sign in with your credentials.", "Registration Successful", JOptionPane.INFORMATION_MESSAGE);
                clearAllFields();
                parentFrame.showSignIn();
            } else {
                JOptionPane.showMessageDialog(this, "This email address is already registered.\nPlease use a different email or sign in instead.", "Email Already Exists", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            if (ex.getMessage().contains("UNIQUE constraint failed")) {
                JOptionPane.showMessageDialog(this, "This email address is already registered.\nPlease use a different email or sign in instead.", "Email Already Exists", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void clearAllFields() {
        nameField.setText("");
        usernameField.setText("");
        gmailField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        roleCombo.setSelectedIndex(0);
        showPasswordBtn.setSelected(false);
        showConfirmPasswordBtn.setSelected(false);
        togglePasswordVisibility(passwordField, showPasswordBtn);
        togglePasswordVisibility(confirmPasswordField, showConfirmPasswordBtn);
    }
}
