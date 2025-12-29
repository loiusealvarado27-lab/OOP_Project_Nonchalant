package com.byteaid.appointment.ui;

import com.byteaid.appointment.model.User;
import com.byteaid.appointment.service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

public class AuthFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JToggleButton showPasswordBtn;
    private JComboBox<String> roleCombo;
    private JLabel feedbackLabel;
    private UserService userService;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public AuthFrame() {
        this.userService = new UserService();
        setTitle("ByteAid - Fast, Reliable Device Support");
        setSize(520, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        // Enable accessibility
        getAccessibleContext().setAccessibleName("ByteAid Login Window");
        getAccessibleContext().setAccessibleDescription("Login to ByteAid device support system");
        
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(createGradientBackground());
        
        cardPanel.add(createModernSignInPanel(), "signin");
        cardPanel.add(createRegisterPanel(), "register");
        
        add(cardPanel);
        setVisible(true);
    }
    
    private Color createGradientBackground() {
        return new Color(245, 247, 250);
    }
    
    private JPanel createModernSignInPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(createGradientBackground());
        
        // Center container
        JPanel centerContainer = new JPanel(new GridBagLayout());
        centerContainer.setBackground(createGradientBackground());
        
        // Main card with shadow effect
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230), 1),
            new EmptyBorder(40, 40, 40, 40)));
        card.setPreferredSize(new Dimension(440, 620));
        
        // Header
        card.add(createBrandHeader());
        card.add(Box.createVerticalStrut(30));
        
        // Feedback area
        feedbackLabel = new JLabel(" ");
        feedbackLabel.setFont(new Font("Roboto", Font.PLAIN, 13));
        feedbackLabel.setForeground(new Color(220, 53, 69));
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        feedbackLabel.setVisible(false);
        card.add(feedbackLabel);
        card.add(Box.createVerticalStrut(10));
        
        // Form
        card.add(createAccessibleForm());
        card.add(Box.createVerticalStrut(25));
        
        // Actions
        card.add(createProfessionalActions());
        
        centerContainer.add(card);
        mainPanel.add(centerContainer, BorderLayout.CENTER);
        return mainPanel;
    }
    
    private JPanel createBrandHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(Color.WHITE);
        
        // Logo
        JLabel logoLabel = new JLabel("🔧", JLabel.CENTER);
        logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        logoLabel.setForeground(new Color(0, 123, 255));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Brand name
        JLabel brandLabel = new JLabel("ByteAid", JLabel.CENTER);
        brandLabel.setFont(new Font("Roboto", Font.BOLD, 32));
        brandLabel.setForeground(new Color(33, 37, 41));
        brandLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Tagline
        JLabel taglineLabel = new JLabel("Fast, Reliable Device Support", JLabel.CENTER);
        taglineLabel.setFont(new Font("Open Sans", Font.PLAIN, 16));
        taglineLabel.setForeground(new Color(108, 117, 125));
        taglineLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        header.add(logoLabel);
        header.add(Box.createVerticalStrut(15));
        header.add(brandLabel);
        header.add(Box.createVerticalStrut(8));
        header.add(taglineLabel);
        
        return header;
    }
    
    private JPanel createAccessibleForm() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);
        
        // Username field
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Roboto", Font.BOLD, 14));
        userLabel.setForeground(new Color(52, 58, 64));
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        usernameField = new JTextField();
        usernameField.setFont(new Font("Open Sans", Font.PLAIN, 16));
        usernameField.setPreferredSize(new Dimension(360, 50));
        usernameField.setMaximumSize(new Dimension(360, 50));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(206, 212, 218), 2),
            BorderFactory.createEmptyBorder(14, 16, 14, 16)));
        usernameField.getAccessibleContext().setAccessibleName("Username");
        usernameField.getAccessibleContext().setAccessibleDescription("Enter your username");
        
        // Password field with show/hide toggle
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Roboto", Font.BOLD, 14));
        passLabel.setForeground(new Color(52, 58, 64));
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setBackground(Color.WHITE);
        passwordPanel.setMaximumSize(new Dimension(360, 50));
        
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Open Sans", Font.PLAIN, 16));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(206, 212, 218), 2),
            BorderFactory.createEmptyBorder(14, 16, 14, 45)));
        passwordField.setEchoChar('•');
        passwordField.getAccessibleContext().setAccessibleName("Password");
        passwordField.getAccessibleContext().setAccessibleDescription("Enter your password");
        
        showPasswordBtn = new JToggleButton("👁");
        showPasswordBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        showPasswordBtn.setPreferredSize(new Dimension(40, 50));
        showPasswordBtn.setBorder(BorderFactory.createEmptyBorder());
        showPasswordBtn.setBackground(Color.WHITE);
        showPasswordBtn.setFocusPainted(false);
        showPasswordBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        showPasswordBtn.getAccessibleContext().setAccessibleName("Show Password");
        showPasswordBtn.addActionListener(e -> togglePasswordVisibility());
        
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        passwordPanel.add(showPasswordBtn, BorderLayout.EAST);
        
        // Role selection with icons
        JLabel roleLabel = new JLabel("Role");
        roleLabel.setFont(new Font("Roboto", Font.BOLD, 14));
        roleLabel.setForeground(new Color(52, 58, 64));
        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        String[] roles = {"💼 Customer", "🛡️ Admin"};
        roleCombo = new JComboBox<>(roles);
        roleCombo.setSelectedIndex(0);
        roleCombo.setFont(new Font("Open Sans", Font.PLAIN, 16));
        roleCombo.setPreferredSize(new Dimension(360, 50));
        roleCombo.setMaximumSize(new Dimension(360, 50));
        roleCombo.setBackground(Color.WHITE);
        roleCombo.setBorder(BorderFactory.createLineBorder(new Color(206, 212, 218), 2));
        roleCombo.getAccessibleContext().setAccessibleName("Role Selection");
        roleCombo.getAccessibleContext().setAccessibleDescription("Select your role");
        
        form.add(userLabel);
        form.add(Box.createVerticalStrut(8));
        form.add(usernameField);
        form.add(Box.createVerticalStrut(20));
        form.add(passLabel);
        form.add(Box.createVerticalStrut(8));
        form.add(passwordPanel);
        form.add(Box.createVerticalStrut(20));
        form.add(roleLabel);
        form.add(Box.createVerticalStrut(8));
        form.add(roleCombo);
        
        return form;
    }
    
    private JPanel createProfessionalActions() {
        JPanel actions = new JPanel();
        actions.setLayout(new BoxLayout(actions, BoxLayout.Y_AXIS));
        actions.setBackground(Color.WHITE);
        
        // Primary Sign In button
        JButton signInBtn = new JButton("Sign In");
        signInBtn.setFont(new Font("Roboto", Font.BOLD, 16));
        signInBtn.setPreferredSize(new Dimension(360, 54));
        signInBtn.setMaximumSize(new Dimension(360, 54));
        signInBtn.setBackground(new Color(0, 123, 255));
        signInBtn.setForeground(Color.WHITE);
        signInBtn.setBorder(BorderFactory.createEmptyBorder());
        signInBtn.setFocusPainted(false);
        signInBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signInBtn.getAccessibleContext().setAccessibleName("Sign In Button");
        signInBtn.addActionListener(e -> login());
        
        // Secondary Create Account button
        JButton createAccountBtn = new JButton("Create Account");
        createAccountBtn.setFont(new Font("Roboto", Font.BOLD, 15));
        createAccountBtn.setPreferredSize(new Dimension(360, 50));
        createAccountBtn.setMaximumSize(new Dimension(360, 50));
        createAccountBtn.setBackground(Color.WHITE);
        createAccountBtn.setForeground(new Color(0, 123, 255));
        createAccountBtn.setBorder(BorderFactory.createLineBorder(new Color(0, 123, 255), 2));
        createAccountBtn.setFocusPainted(false);
        createAccountBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createAccountBtn.getAccessibleContext().setAccessibleName("Create Account Button");
        createAccountBtn.addActionListener(e -> cardLayout.show(cardPanel, "register"));
        
        // Forgot Password link
        JButton forgotPasswordBtn = new JButton("Forgot Password?");
        forgotPasswordBtn.setFont(new Font("Open Sans", Font.PLAIN, 13));
        forgotPasswordBtn.setForeground(new Color(108, 117, 125));
        forgotPasswordBtn.setBackground(Color.WHITE);
        forgotPasswordBtn.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        forgotPasswordBtn.setFocusPainted(false);
        forgotPasswordBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPasswordBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        forgotPasswordBtn.getAccessibleContext().setAccessibleName("Forgot Password Link");
        forgotPasswordBtn.addActionListener(e -> showForgotPasswordDialog());
        
        actions.add(signInBtn);
        actions.add(Box.createVerticalStrut(15));
        actions.add(createAccountBtn);
        actions.add(Box.createVerticalStrut(10));
        actions.add(forgotPasswordBtn);
        
        return actions;
    }
    
    private void togglePasswordVisibility() {
        if (showPasswordBtn.isSelected()) {
            passwordField.setEchoChar((char) 0);
            showPasswordBtn.setText("🙈");
            showPasswordBtn.getAccessibleContext().setAccessibleName("Hide Password");
        } else {
            passwordField.setEchoChar('•');
            showPasswordBtn.setText("👁");
            showPasswordBtn.getAccessibleContext().setAccessibleName("Show Password");
        }
    }
    
    private void showFeedback(String message, boolean isError) {
        feedbackLabel.setText(message);
        feedbackLabel.setForeground(isError ? new Color(220, 53, 69) : new Color(40, 167, 69));
        feedbackLabel.setVisible(true);
        
        // Auto-hide after 5 seconds
        Timer timer = new Timer(5000, e -> feedbackLabel.setVisible(false));
        timer.setRepeats(false);
        timer.start();
    }
    
    private void showForgotPasswordDialog() {
        JOptionPane.showMessageDialog(this, 
            "Please contact your system administrator to reset your password.", 
            "Password Recovery", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private JPanel createRegisterPanel() {
        return new RegisterPanel(this);
    }
    
    public void showSignIn() {
        cardLayout.show(cardPanel, "signin");
    }
    
    private void login() {
        String username = usernameField.getText().trim();
        String password = String.valueOf(passwordField.getPassword());
        String selectedRole = (String) roleCombo.getSelectedItem();
        
        // Extract role from icon string
        String role = "Customer";
        if (selectedRole.contains("Admin")) role = "Admin";

        if (username.isEmpty() || password.isEmpty()) {
            showFeedback("Please enter both username and password.", true);
            return;
        }

        try {
            User user = userService.authenticate(username, password, role);
            if (user != null) {
                showFeedback("Login successful! Redirecting...", false);
                
                // Delay to show success message
                Timer timer = new Timer(1000, e -> {
                    dispose();
                    if (user.getRole().equalsIgnoreCase("admin")) {
                        new AdminDashboard(user.getId());
                    } else {
                        new UserDashboard(user.getId());
                    }
                });
                timer.setRepeats(false);
                timer.start();
            } else {
                showFeedback("Invalid credentials or role mismatch.", true);
            }
        } catch (SQLException ex) {
            showFeedback("Connection error: " + ex.getMessage(), true);
        }
    }
}
