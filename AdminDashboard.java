package com.byteaid.appointment.ui;

import com.byteaid.appointment.model.Appointment;
import com.byteaid.appointment.service.AppointmentService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class AdminDashboard extends JFrame {
    private final AppointmentService appointmentService;

    public AdminDashboard(int adminId) {
        this.appointmentService = new AppointmentService();
        initializeComponents();
    }

    private void initializeComponents() {
        setTitle("ByteAid - Admin Dashboard");
        setSize(1300, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Blue Header with ByteAid branding (matching Customer Dashboard)
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
        
        // Logo and title (matching Customer Dashboard)
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        logoPanel.setBackground(new Color(0, 123, 255));
        
        JLabel logoLabel = new JLabel("🔧");
        logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        
        JLabel titleLabel = new JLabel("ByteAid Admin");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("System Administration Panel");
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
        sidebar.setPreferredSize(new Dimension(280, 0));
        
        // Navigation buttons
        JButton appointmentsBtn = createSidebarButton("📋 View All Appointments", new Color(0, 123, 255));
        appointmentsBtn.addActionListener(e -> new AdminAppointmentsFrame());
        
        JButton assignBtn = createSidebarButton("👥 Assign Technician", new Color(255, 193, 7));
        assignBtn.addActionListener(e -> showAssignTechnicianWindow());
        
        JButton updateBtn = createSidebarButton("⚙️ Update Status", new Color(40, 167, 69));
        updateBtn.addActionListener(e -> showUpdateStatusWindow());
        
        JButton viewHistoryBtn = createSidebarButton("📄 View History", new Color(73, 80, 87));
        viewHistoryBtn.addActionListener(e -> new ViewHistoryFrame());
        
        JButton manageUsersBtn = createSidebarButton("👥 Manage Users", new Color(102, 16, 242));
        manageUsersBtn.addActionListener(e -> new ManageUsersFrame());
        
        JButton logoutBtn = createSidebarButton("🚪 Logout", new Color(220, 53, 69));
        logoutBtn.addActionListener(e -> {
            dispose();
            new AuthFrame();
        });
        
        sidebar.add(appointmentsBtn);
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(assignBtn);
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(updateBtn);
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(viewHistoryBtn);
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(manageUsersBtn);
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(logoutBtn);
        sidebar.add(Box.createVerticalGlue());
        
        return sidebar;
    }
    
    private JButton createSidebarButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Roboto", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(240, 45));
        button.setMaximumSize(new Dimension(240, 45));
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
        
        // System Overview Section
        JPanel overviewPanel = createSystemOverview();
        content.add(overviewPanel, BorderLayout.NORTH);
        
        // Action Buttons Section
        JPanel actionsPanel = createActionButtons();
        content.add(actionsPanel, BorderLayout.CENTER);
        
        return content;
    }
    
    private JPanel createSystemOverview() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 247, 250));
        panel.setBorder(new EmptyBorder(0, 0, 30, 0));
        
        JLabel titleLabel = new JLabel("System Overview");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 22));
        titleLabel.setForeground(new Color(33, 37, 41));
        
        // Summary Cards
        JPanel summaryPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        summaryPanel.setBackground(new Color(245, 247, 250));
        summaryPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        // Get system statistics
        int[] stats = getSystemStats();
        
        summaryPanel.add(createSummaryCard("Total Appointments", String.valueOf(stats[0]), new Color(0, 123, 255)));
        summaryPanel.add(createSummaryCard("Pending Tasks", String.valueOf(stats[1]), new Color(255, 193, 7)));
        summaryPanel.add(createSummaryCard("Completed", String.valueOf(stats[2]), new Color(40, 167, 69)));
        summaryPanel.add(createSummaryCard("Available Techs", "5", new Color(108, 117, 125)));
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(summaryPanel, BorderLayout.CENTER);
        
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
        countLabel.setFont(new Font("Roboto", Font.BOLD, 32));
        countLabel.setForeground(accentColor);
        
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Open Sans", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(108, 117, 125));
        
        card.add(countLabel, BorderLayout.CENTER);
        card.add(titleLabel, BorderLayout.SOUTH);
        
        return card;
    }
    
    private JPanel createActionButtons() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 247, 250));
        
        JLabel titleLabel = new JLabel("Quick Actions");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 22));
        titleLabel.setForeground(new Color(33, 37, 41));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        JPanel statusPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        statusPanel.setBackground(new Color(245, 247, 250));
        
        int[] stats = getSystemStats();
        statusPanel.add(createStatusCard("Pending Devices", String.valueOf(stats[1]), "Awaiting assignment or work", new Color(255, 193, 7)));
        statusPanel.add(createStatusCard("In Progress", String.valueOf(getInProgressCount()), "Currently being worked on", new Color(0, 123, 255)));
        statusPanel.add(createStatusCard("Ready for Pickup", String.valueOf(getReadyForPickupCount()), "Completed and ready", new Color(40, 167, 69)));
        statusPanel.add(createStatusCard("System Status", "Online", "All systems operational", new Color(108, 117, 125)));
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(statusPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createStatusCard(String title, String value, String description, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 225, 230), 1),
            new EmptyBorder(20, 15, 20, 15)
        ));
        
        JLabel valueLabel = new JLabel(value, JLabel.CENTER);
        valueLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        valueLabel.setForeground(accentColor);
        
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 14));
        titleLabel.setForeground(new Color(33, 37, 41));
        
        JLabel descLabel = new JLabel("<html><center>" + description + "</center></html>", JLabel.CENTER);
        descLabel.setFont(new Font("Open Sans", Font.PLAIN, 11));
        descLabel.setForeground(new Color(108, 117, 125));
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);
        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(3));
        textPanel.add(descLabel);
        
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(textPanel, BorderLayout.SOUTH);
        
        return card;
    }
    
    private int[] getSystemStats() {
        try {
            List<Appointment> activeAppointments = appointmentService.getActiveAppointments();
            int total = activeAppointments.size();
            int pending = 0;
            int completed = 0;
            
            for (Appointment apt : activeAppointments) {
                String status = apt.getStatus();
                if ("Pending".equals(status) || "Assigned".equals(status)) {
                    pending++;
                } else if ("Completed".equals(status) || "Device Picked Up".equals(status)) {
                    completed++;
                }
            }
            
            return new int[]{total, pending, completed};
        } catch (SQLException e) {
            return new int[]{0, 0, 0};
        }
    }
    
    private int getInProgressCount() {
        try {
            List<Appointment> appointments = appointmentService.getActiveAppointments();
            int count = 0;
            for (Appointment apt : appointments) {
                String status = apt.getStatus();
                if ("Working".equals(status)) {
                    count++;
                }
            }
            return count;
        } catch (SQLException e) {
            return 0;
        }
    }
    
    private int getReadyForPickupCount() {
        try {
            List<Appointment> appointments = appointmentService.getActiveAppointments();
            int count = 0;
            for (Appointment apt : appointments) {
                String status = apt.getStatus();
                if ("Completed".equals(status) || "Device Picked Up".equals(status)) {
                    count++;
                }
            }
            return count;
        } catch (SQLException e) {
            return 0;
        }
    }
    
    private void showAssignTechnicianWindow() {
        AssignTechnicianFrame frame = new AssignTechnicianFrame();
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                refreshDashboard();
            }
        });
    }
    
    private void showUpdateStatusWindow() {
        UpdateStatusFrame frame = new UpdateStatusFrame();
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                refreshDashboard();
            }
        });
    }
    

    
    private void refreshDashboard() {
        SwingUtilities.invokeLater(() -> {
            getContentPane().removeAll();
            initializeComponents();
            revalidate();
            repaint();
        });
        JOptionPane.showMessageDialog(this, "Dashboard refreshed successfully!", "Refresh Complete", JOptionPane.INFORMATION_MESSAGE);
    }
}
