package com.byteaid.appointment.ui;

import com.byteaid.appointment.model.Appointment;
import com.byteaid.appointment.service.AppointmentService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class UserDashboard extends JFrame {
    private int userId;
    private AppointmentService appointmentService;

    public UserDashboard(int userId) {
        this.userId = userId;
        this.appointmentService = new AppointmentService();
        initializeComponents();
    }

    private void initializeComponents() {
        setTitle("ByteAid - Customer Dashboard");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Blue Header with ByteAid branding
        add(createHeader(), BorderLayout.NORTH);
        
        // Main content with sidebar and dashboard
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(new Color(245, 247, 250));
        
        // Left Sidebar
        mainContent.add(createSidebar(), BorderLayout.WEST);
        
        // Dashboard Content
        mainContent.add(createDashboardContent(), BorderLayout.CENTER);
        
        add(mainContent, BorderLayout.CENTER);
        setVisible(true);
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0, 123, 255));
        header.setBorder(new EmptyBorder(15, 25, 15, 25));
        
        // Logo and title
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        logoPanel.setBackground(new Color(0, 123, 255));
        
        JLabel logoLabel = new JLabel("🔧");
        logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        
        JLabel titleLabel = new JLabel("ByteAid");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Device Repair & Support");
        subtitleLabel.setFont(new Font("Open Sans", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(220, 230, 255));
        
        JPanel brandPanel = new JPanel();
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        brandPanel.setBackground(new Color(0, 123, 255));
        brandPanel.add(titleLabel);
        brandPanel.add(subtitleLabel);
        
        logoPanel.add(logoLabel);
        logoPanel.add(Box.createHorizontalStrut(10));
        logoPanel.add(brandPanel);
        
        header.add(logoPanel, BorderLayout.WEST);
        return header;
    }
    
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Color.WHITE);
        sidebar.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 225, 230), 1),
            new EmptyBorder(25, 20, 25, 20)
        ));
        sidebar.setPreferredSize(new Dimension(250, 0));
        
        // Navigation buttons
        JButton scheduleBtn = createSidebarButton("📅 Schedule Appointment", new Color(0, 123, 255));
        scheduleBtn.addActionListener(e -> new BookAppointmentFrame(userId));
        
        JButton viewBtn = createSidebarButton("📋 View My Appointments", new Color(40, 167, 69));
        viewBtn.addActionListener(e -> new ViewAppointmentsFrame(userId));
        
        JButton logoutBtn = createSidebarButton("🚪 Logout", new Color(220, 53, 69));
        logoutBtn.addActionListener(e -> {
            dispose();
            new AuthFrame();
        });
        
        sidebar.add(scheduleBtn);
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(viewBtn);
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(logoutBtn);
        sidebar.add(Box.createVerticalGlue());
        
        return sidebar;
    }
    
    private JButton createSidebarButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Roboto", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(210, 45));
        button.setMaximumSize(new Dimension(210, 45));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        return button;
    }
    
    private JPanel createDashboardContent() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(new Color(245, 247, 250));
        content.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        // Welcome Section
        JPanel welcomePanel = createWelcomeSection();
        content.add(welcomePanel, BorderLayout.NORTH);
        
        // Main dashboard area
        JPanel dashboardArea = new JPanel(new BorderLayout());
        dashboardArea.setBackground(new Color(245, 247, 250));
        
        // How to Use Section
        JPanel howToUsePanel = createHowToUseSection();
        dashboardArea.add(howToUsePanel, BorderLayout.NORTH);
        
        // Summary Cards
        JPanel summaryPanel = createSummaryCards();
        dashboardArea.add(summaryPanel, BorderLayout.CENTER);
        
        // Device Ready Notification (only if device is ready)
        JPanel notificationPanel = createDeviceReadyNotification();
        if (notificationPanel != null) {
            dashboardArea.add(notificationPanel, BorderLayout.SOUTH);
        }
        
        content.add(dashboardArea, BorderLayout.CENTER);
        return content;
    }
    
    private JPanel createWelcomeSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 247, 250));
        panel.setBorder(new EmptyBorder(0, 0, 25, 0));
        
        JLabel welcomeLabel = new JLabel("Welcome back!");
        welcomeLabel.setFont(new Font("Roboto", Font.BOLD, 28));
        welcomeLabel.setForeground(new Color(33, 37, 41));
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Manage your device repair appointments and track progress");
        subtitleLabel.setFont(new Font("Open Sans", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(108, 117, 125));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(welcomeLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(subtitleLabel);
        
        return panel;
    }
    
    private JPanel createHowToUseSection() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 225, 230), 1),
            new EmptyBorder(20, 25, 20, 25)
        ));
        
        JLabel titleLabel = new JLabel("How to use ByteAid");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 18));
        titleLabel.setForeground(new Color(33, 37, 41));
        
        JPanel stepsPanel = new JPanel();
        stepsPanel.setLayout(new BoxLayout(stepsPanel, BoxLayout.Y_AXIS));
        stepsPanel.setBackground(Color.WHITE);
        stepsPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        String[] steps = {
            "1. Click 'Schedule Appointment' to book a new repair session",
            "2. Fill out the appointment form with your device details",
            "3. Track your appointment status in 'View My Appointments'",
            "4. Receive notifications when your device is ready for pickup"
        };
        
        for (String step : steps) {
            JLabel stepLabel = new JLabel(step);
            stepLabel.setFont(new Font("Open Sans", Font.PLAIN, 14));
            stepLabel.setForeground(new Color(73, 80, 87));
            stepLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            stepLabel.setBorder(new EmptyBorder(3, 0, 3, 0));
            stepsPanel.add(stepLabel);
        }
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(stepsPanel, BorderLayout.CENTER);
        
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(new Color(245, 247, 250));
        containerPanel.setBorder(new EmptyBorder(0, 0, 25, 0));
        containerPanel.add(panel, BorderLayout.CENTER);
        
        return containerPanel;
    }
    
    private JPanel createSummaryCards() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 0));
        panel.setBackground(new Color(245, 247, 250));
        panel.setBorder(new EmptyBorder(0, 0, 25, 0));
        
        // Get appointment counts
        int[] counts = getAppointmentCounts();
        
        panel.add(createSummaryCard("My Appointments", String.valueOf(counts[0]), new Color(0, 123, 255)));
        panel.add(createSummaryCard("Pending", String.valueOf(counts[1]), new Color(255, 193, 7)));
        panel.add(createSummaryCard("Completed", String.valueOf(counts[2]), new Color(40, 167, 69)));
        
        return panel;
    }
    
    private JPanel createSummaryCard(String title, String count, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 225, 230), 1),
            new EmptyBorder(25, 20, 25, 20)
        ));
        
        JLabel countLabel = new JLabel(count, JLabel.CENTER);
        countLabel.setFont(new Font("Roboto", Font.BOLD, 36));
        countLabel.setForeground(accentColor);
        
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Open Sans", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(108, 117, 125));
        
        card.add(countLabel, BorderLayout.CENTER);
        card.add(titleLabel, BorderLayout.SOUTH);
        
        return card;
    }
    
    private JPanel createDeviceReadyNotification() {
        try {
            List<Appointment> completedAppointments = appointmentService.getUserAppointments(userId);
            Appointment readyDevice = null;
            
            // Find the most recent completed or ready for pickup appointment
            for (Appointment appointment : completedAppointments) {
                String status = appointment.getStatus();
                if ("Completed".equals(status) || "Device Picked Up".equals(status)) {
                    readyDevice = appointment;
                    break; // Get the first (most recent) ready appointment
                }
            }
            
            if (readyDevice == null) {
                return null; // No notification if no completed devices
            }
            
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(new Color(212, 237, 218));
            panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(195, 230, 203), 1),
                new EmptyBorder(15, 20, 15, 20)
            ));
            
            JLabel iconLabel = new JLabel("✓");
            iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            iconLabel.setForeground(new Color(21, 87, 36));
            
            String deviceInfo = readyDevice.getDevice() + " (" + readyDevice.getIssue() + ")";
            String formattedTime = formatTimeTo12Hour(readyDevice.getTime());
            String message = "Your device is ready for pickup! " + deviceInfo + " - Completed on " + 
                           readyDevice.getDate() + " at " + formattedTime + ". You can pick it up anytime during business hours (9:00 AM - 6:00 PM).";
            
            JLabel messageLabel = new JLabel("<html><div style='width:600px'>" + message + "</div></html>");
            messageLabel.setFont(new Font("Open Sans", Font.BOLD, 14));
            messageLabel.setForeground(new Color(21, 87, 36));
            
            JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            contentPanel.setBackground(new Color(212, 237, 218));
            contentPanel.add(iconLabel);
            contentPanel.add(messageLabel);
            
            panel.add(contentPanel, BorderLayout.CENTER);
            return panel;
            
        } catch (SQLException e) {
            return null; // No notification on error
        }
    }
    
    private int[] getAppointmentCounts() {
        try {
            // Returns [total, pending, completed]
            int total = appointmentService.getUserAppointmentCount(userId);
            int pending = appointmentService.getPendingAppointmentCount(userId);
            int completed = appointmentService.getCompletedAppointmentCount(userId);
            return new int[]{total, pending, completed};
        } catch (SQLException e) {
            return new int[]{0, 0, 0};
        }
    }
    
    private String formatTimeTo12Hour(String time24) {
        if (time24 == null || time24.isEmpty()) {
            return time24;
        }
        
        try {
            String[] parts = time24.split(":");
            int hour = Integer.parseInt(parts[0]);
            String minute = parts[1];
            
            String ampm = "AM";
            if (hour == 0) {
                hour = 12;
            } else if (hour == 12) {
                ampm = "PM";
            } else if (hour > 12) {
                hour -= 12;
                ampm = "PM";
            }
            
            return hour +":" + minute + " " + ampm;
        } catch (Exception e) {
            return time24; // Return original if parsing fails
        }
    }
    
    public void refreshDashboard() {
        // Refresh the dashboard content when called
        SwingUtilities.invokeLater(() -> {
            getContentPane().removeAll();
            initializeComponents();
            revalidate();
            repaint();
        });
    }
}
