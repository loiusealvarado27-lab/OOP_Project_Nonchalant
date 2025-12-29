package com.byteaid.appointment.ui;

import com.byteaid.appointment.model.Appointment;
import com.byteaid.appointment.service.AppointmentService;
import com.byteaid.appointment.service.TechnicianService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import javax.swing.SwingUtilities;

public class AssignTechnicianFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private final AppointmentService appointmentService;
    private final TechnicianService technicianService;
    private JComboBox<String> technicianCombo;

    public AssignTechnicianFrame() {
        this.appointmentService = new AppointmentService();
        this.technicianService = new TechnicianService();
        initializeComponents();
    }

    private void initializeComponents() {
        setTitle("Assign Technician");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 247, 250));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Header
        JLabel titleLabel = new JLabel("Assign Technician to Appointments");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setForeground(new Color(33, 37, 41));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Customer ID", "Problem", "Device", "Date", "Time", "Status"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setFont(new Font("Open Sans", Font.PLAIN, 13));
        table.setRowHeight(35);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(255, 193, 7));
        table.getTableHeader().setForeground(Color.WHITE);

        loadUnassignedAppointments();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(new Color(220, 225, 230), 1));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Assignment Panel
        JPanel assignPanel = new JPanel(new FlowLayout());
        assignPanel.setBackground(new Color(245, 247, 250));
        assignPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        JLabel techLabel = new JLabel("Technician:");
        techLabel.setFont(new Font("Roboto", Font.BOLD, 14));

        technicianCombo = new JComboBox<>();
        loadAvailableTechnicians();
        technicianCombo.setFont(new Font("Open Sans", Font.PLAIN, 14));
        technicianCombo.setPreferredSize(new Dimension(200, 35));

        JButton assignBtn = new JButton("Assign Technician");
        assignBtn.setFont(new Font("Roboto", Font.BOLD, 14));
        assignBtn.setPreferredSize(new Dimension(150, 35));
        assignBtn.setBackground(new Color(255, 193, 7));
        assignBtn.setForeground(Color.WHITE);
        assignBtn.setBorder(BorderFactory.createEmptyBorder());
        assignBtn.setFocusPainted(false);
        assignBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        assignBtn.addActionListener(e -> assignTechnician());

        JButton closeBtn = new JButton("Close");
        closeBtn.setFont(new Font("Roboto", Font.BOLD, 14));
        closeBtn.setPreferredSize(new Dimension(100, 35));
        closeBtn.setBackground(new Color(108, 117, 125));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setBorder(BorderFactory.createEmptyBorder());
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> dispose());

        assignPanel.add(techLabel);
        assignPanel.add(technicianCombo);
        assignPanel.add(assignBtn);
        assignPanel.add(closeBtn);

        mainPanel.add(assignPanel, BorderLayout.SOUTH);
        add(mainPanel);
        setVisible(true);
    }

    private void loadUnassignedAppointments() {
        model.setRowCount(0);
        try {
            List<Appointment> appointments = appointmentService.getActiveAppointments();
            for (Appointment appointment : appointments) {
                String status = appointment.getStatus() != null ? appointment.getStatus() : "Pending";
                // Show all active appointments that can be assigned/reassigned
                if (!"Completed".equals(status) && !"Cancelled".equals(status) && !"Archived".equals(status)) {
                    Object[] row = {
                        appointment.getId(),
                        appointment.getUserId(),
                        appointment.getIssue(),
                        appointment.getDevice(),
                        appointment.getDate(),
                        appointment.getTime(),
                        status
                    };
                    model.addRow(row);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading appointments: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadAvailableTechnicians() {
        technicianCombo.removeAllItems();
        List<String> availableTechnicians = technicianService.getAvailableTechnicians();
        for (String technician : availableTechnicians) {
            technicianCombo.addItem(technician);
        }
        if (technicianCombo.getItemCount() > 0) {
            technicianCombo.setSelectedIndex(0);
        }
    }
    
    private void assignTechnician() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to assign.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String technician = (String) technicianCombo.getSelectedItem();
        if (technician == null) {
            JOptionPane.showMessageDialog(this, "Please select a technician.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int appointmentId = (Integer) model.getValueAt(selectedRow, 0);
        String currentStatus = (String) model.getValueAt(selectedRow, 6);

        try {
            if (appointmentService.assignTechnician(appointmentId, technician)) {
                JOptionPane.showMessageDialog(this, 
                    "Technician assigned successfully!\n\n" +
                    "Appointment ID: " + appointmentId + "\n" +
                    "Status: " + currentStatus + "\n" +
                    "Assigned Technician: " + technician, 
                    "Assignment Successful", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadUnassignedAppointments(); // Refresh the table
                refreshParentDashboard();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to assign technician. The appointment may already be completed or cancelled.", 
                    "Assignment Failed", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void refreshParentDashboard() {
        // Trigger refresh of any parent dashboard windows
        java.awt.Window[] windows = java.awt.Window.getWindows();
        for (java.awt.Window window : windows) {
            if (window instanceof com.byteaid.appointment.ui.AdminDashboard) {
                SwingUtilities.invokeLater(() -> {
                    window.repaint();
                });
            }
        }
    }
}
