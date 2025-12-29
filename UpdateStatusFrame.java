package com.byteaid.appointment.ui;

import com.byteaid.appointment.model.Appointment;
import com.byteaid.appointment.service.AppointmentService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import javax.swing.SwingUtilities;

public class UpdateStatusFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private AppointmentService appointmentService;

    public UpdateStatusFrame() {
        this.appointmentService = new AppointmentService();
        initializeComponents();
    }

    private void initializeComponents() {
        setTitle("Update Appointment Status");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 247, 250));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Header
        JLabel titleLabel = new JLabel("Update Appointment Status");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setForeground(new Color(33, 37, 41));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Customer", "Problem", "Device", "Date", "Time", "Status", "Technician"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setFont(new Font("Open Sans", Font.PLAIN, 12));
        table.setRowHeight(35);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(40, 167, 69));
        table.getTableHeader().setForeground(Color.WHITE);

        // Status cell renderer
        table.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(CENTER);
                if (!isSelected) {
                    String status = (String) value;
                    switch (status != null ? status : "Pending") {
                        case "Pending":
                            c.setBackground(new Color(255, 243, 205));
                            c.setForeground(new Color(133, 100, 4));
                            break;
                        case "Confirmed":
                        case "Assigned":
                            c.setBackground(new Color(212, 237, 218));
                            c.setForeground(new Color(21, 87, 36));
                            break;
                        case "Working":
                        case "In Progress":
                            c.setBackground(new Color(204, 229, 255));
                            c.setForeground(new Color(4, 85, 224));
                            break;
                        case "Completed":
                            c.setBackground(new Color(195, 230, 203));
                            c.setForeground(new Color(21, 87, 36));
                            break;
                        case "Device Picked Up":
                            c.setBackground(new Color(144, 238, 144));
                            c.setForeground(new Color(0, 100, 0));
                            break;
                        case "Cancelled":
                            c.setBackground(new Color(248, 215, 218));
                            c.setForeground(new Color(114, 28, 36));
                            break;
                        default:
                            c.setBackground(Color.WHITE);
                            c.setForeground(Color.BLACK);
                    }
                }
                return c;
            }
        });

        loadActiveAppointments();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(new Color(220, 225, 230), 1));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Control Panel
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.setBackground(new Color(245, 247, 250));
        controlPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        JButton updateBtn = new JButton("Update Status");
        updateBtn.setFont(new Font("Roboto", Font.BOLD, 14));
        updateBtn.setPreferredSize(new Dimension(130, 35));
        updateBtn.setBackground(new Color(40, 167, 69));
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setBorder(BorderFactory.createEmptyBorder());
        updateBtn.setFocusPainted(false);
        updateBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateBtn.addActionListener(e -> updateStatus());

        JButton moveToHistoryBtn = new JButton("Move to History");
        moveToHistoryBtn.setFont(new Font("Roboto", Font.BOLD, 14));
        moveToHistoryBtn.setPreferredSize(new Dimension(140, 35));
        moveToHistoryBtn.setBackground(new Color(255, 193, 7));
        moveToHistoryBtn.setForeground(Color.WHITE);
        moveToHistoryBtn.setBorder(BorderFactory.createEmptyBorder());
        moveToHistoryBtn.setFocusPainted(false);
        moveToHistoryBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        moveToHistoryBtn.setToolTipText("Move appointments with 'Device Picked Up' or 'Completed' status to history");
        moveToHistoryBtn.addActionListener(e -> movePickedUpToHistory());

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setFont(new Font("Roboto", Font.BOLD, 14));
        refreshBtn.setPreferredSize(new Dimension(100, 35));
        refreshBtn.setBackground(new Color(0, 123, 255));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setBorder(BorderFactory.createEmptyBorder());
        refreshBtn.setFocusPainted(false);
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.addActionListener(e -> loadActiveAppointments());

        JButton closeBtn = new JButton("Close");
        closeBtn.setFont(new Font("Roboto", Font.BOLD, 14));
        closeBtn.setPreferredSize(new Dimension(100, 35));
        closeBtn.setBackground(new Color(108, 117, 125));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setBorder(BorderFactory.createEmptyBorder());
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> dispose());

        controlPanel.add(updateBtn);
        controlPanel.add(moveToHistoryBtn);
        controlPanel.add(refreshBtn);
        controlPanel.add(closeBtn);

        mainPanel.add(controlPanel, BorderLayout.SOUTH);
        add(mainPanel);
        setVisible(true);
    }

    private void loadActiveAppointments() {
        model.setRowCount(0);
        try {
            List<Appointment> appointments = appointmentService.getActiveAppointments();
            for (Appointment appointment : appointments) {
                Object[] row = {
                    appointment.getId(),
                    appointment.getUserId(),
                    appointment.getIssue(),
                    appointment.getDevice(),
                    appointment.getDate(),
                    appointment.getTime(),
                    appointment.getStatus() != null ? appointment.getStatus() : "Pending",
                    appointment.getAssignedTechnicianName() != null ? appointment.getAssignedTechnicianName() : "Unassigned"
                };
                model.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading appointments: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStatus() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        proceedWithStatusUpdate(selectedRow);
    }

    private void proceedWithStatusUpdate(int selectedRow) {
        int appointmentId = (Integer) model.getValueAt(selectedRow, 0);
        String currentStatus = (String) model.getValueAt(selectedRow, 6);

        String[] statuses = {"Pending", "Assigned", "Working", "Completed", "Cancelled", "Device Picked Up"};

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Appointment ID: " + appointmentId));
        panel.add(new JLabel("Current Status: " + currentStatus));
        panel.add(new JLabel("Select new status:"));

        JComboBox<String> statusCombo = new JComboBox<>(statuses);
        statusCombo.setSelectedItem(currentStatus);
        panel.add(statusCombo);

        int result = JOptionPane.showConfirmDialog(this, panel, "Update Status", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String newStatus = (String) statusCombo.getSelectedItem();
            if (!newStatus.equals(currentStatus)) {
                try {
                    if (appointmentService.updateAppointmentStatus(appointmentId, newStatus)) {
                        JOptionPane.showMessageDialog(this, "Status updated successfully!\n\nAppointment ID: " + appointmentId + "\nOld Status: " + currentStatus + "\nNew Status: " + newStatus, "Update Successful", JOptionPane.INFORMATION_MESSAGE);
                        loadActiveAppointments();
                        refreshParentDashboard();
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to update status. The appointment may not exist or be in an invalid state.", "Update Failed", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "No changes made - status remains: " + currentStatus, "No Update", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private void movePickedUpToHistory() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to move to history.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int appointmentId = (Integer) model.getValueAt(selectedRow, 0);
        String currentStatus = (String) model.getValueAt(selectedRow, 6);
        
        if (!"Device Picked Up".equals(currentStatus) && !"Completed".equals(currentStatus)) {
            JOptionPane.showMessageDialog(this, 
                "Only appointments with 'Device Picked Up' or 'Completed' status can be moved to history.\n\n" +
                "Current status: " + currentStatus + "\n" +
                "Please update the status first if the device has been picked up.", 
                "Invalid Status", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Move appointment #" + appointmentId + " to history?\n\n" +
            "Current Status: " + currentStatus + "\n" +
            "This will archive the appointment and remove it from active appointments.", 
            "Confirm Move to History", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Update the moveToHistory method to handle both statuses
                if (appointmentService.moveToHistoryByStatus(appointmentId)) {
                    JOptionPane.showMessageDialog(this, 
                        "Appointment #" + appointmentId + " moved to history successfully!\n\n" +
                        "The appointment has been archived and is now accessible in the View History section.", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadActiveAppointments();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to move appointment to history.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void refreshParentDashboard() {
        // Trigger refresh of any parent dashboard windows
        java.awt.Window[] windows = java.awt.Window.getWindows();
        for (java.awt.Window window : windows) {
            if (window instanceof com.byteaid.appointment.ui.AdminDashboard) {
                // Refresh admin dashboard
                SwingUtilities.invokeLater(() -> {
                    window.repaint();
                });
            }
        }
    }
}
