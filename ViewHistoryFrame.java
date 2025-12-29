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
import java.util.ArrayList;
import java.util.List;

public class ViewHistoryFrame extends JFrame {
    private JTable historyTable;
    private DefaultTableModel historyModel;
    private AppointmentService appointmentService;

    public ViewHistoryFrame() {
        this.appointmentService = new AppointmentService();
        initializeComponents();
    }

    private void initializeComponents() {
        setTitle("Appointment History - Archived Records");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 247, 250));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Header
        JLabel titleLabel = new JLabel("Appointment History");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setForeground(new Color(33, 37, 41));
        
        JLabel subtitleLabel = new JLabel("View and manage all completed, picked-up, cancelled, and archived appointments");
        subtitleLabel.setFont(new Font("Open Sans", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(108, 117, 125));
        
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(245, 247, 250));
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subtitleLabel);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // History Table
        String[] columns = {"ID", "Customer ID", "Problem", "Device", "Date", "Time", "Final Status", "Technician", "Archived Date"};
        historyModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        historyTable = new JTable(historyModel);
        historyTable.setFont(new Font("Open Sans", Font.PLAIN, 12));
        historyTable.setRowHeight(35);
        historyTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        historyTable.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 12));
        historyTable.getTableHeader().setBackground(new Color(108, 117, 125));
        historyTable.getTableHeader().setForeground(Color.WHITE);

        // Status cell renderer for history
        historyTable.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(CENTER);
                if (!isSelected) {
                    String status = (String) value;
                    if ("Completed".equals(status)) {
                        c.setBackground(new Color(195, 230, 203));
                        c.setForeground(new Color(21, 87, 36));
                    } else if ("Cancelled".equals(status)) {
                        c.setBackground(new Color(248, 215, 218));
                        c.setForeground(new Color(114, 28, 36));
                    } else {
                        c.setBackground(new Color(233, 236, 239));
                        c.setForeground(new Color(73, 80, 87));
                    }
                }
                return c;
            }
        });

        loadHistoryAppointments();

        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.setBorder(new LineBorder(new Color(220, 225, 230), 1));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Control Panel
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.setBackground(new Color(245, 247, 250));
        controlPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        JButton refreshBtn = new JButton("Refresh History");
        refreshBtn.setFont(new Font("Roboto", Font.BOLD, 14));
        refreshBtn.setPreferredSize(new Dimension(130, 35));
        refreshBtn.setBackground(new Color(0, 123, 255));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setBorder(BorderFactory.createEmptyBorder());
        refreshBtn.setFocusPainted(false);
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.addActionListener(e -> {
            loadHistoryAppointments();
            JOptionPane.showMessageDialog(this, "History refreshed!", "Refresh Complete", JOptionPane.INFORMATION_MESSAGE);
        });



        JButton closeBtn = new JButton("Close");
        closeBtn.setFont(new Font("Roboto", Font.BOLD, 14));
        closeBtn.setPreferredSize(new Dimension(100, 35));
        closeBtn.setBackground(new Color(108, 117, 125));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setBorder(BorderFactory.createEmptyBorder());
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> dispose());





        controlPanel.add(refreshBtn);
        controlPanel.add(closeBtn);

        mainPanel.add(controlPanel, BorderLayout.SOUTH);
        add(mainPanel);
        setVisible(true);
    }

    private void loadHistoryAppointments() {
        historyModel.setRowCount(0);
        try {
            // Load both archived appointments and completed/cancelled ones
            List<Appointment> historyAppointments = appointmentService.getHistoryAppointments();
            List<Appointment> completedAppointments = appointmentService.getCompletedAndCancelledAppointments();
            
            // Combine both lists for unified view
            List<Appointment> allHistoryAppointments = new ArrayList<>();
            allHistoryAppointments.addAll(historyAppointments);
            allHistoryAppointments.addAll(completedAppointments);
            
            if (allHistoryAppointments.isEmpty()) {
                // Add a message row if no history exists
                Object[] messageRow = {"No Data", "No archived appointments found", "", "", "", "", "", "", ""};
                historyModel.addRow(messageRow);
            } else {
                for (Appointment appointment : allHistoryAppointments) {
                    String status = appointment.getStatus();
                    String displayStatus = status != null ? status : "Archived";
                    if ("Archived".equals(status)) {
                        displayStatus = "Archived";
                    }
                    
                    Object[] row = {
                        appointment.getId(),
                        appointment.getUserId(),
                        appointment.getIssue(),
                        appointment.getDevice(),
                        appointment.getDate(),
                        appointment.getTime(),
                        displayStatus,
                        appointment.getAssignedTechnicianName() != null ? appointment.getAssignedTechnicianName() : "Unassigned",
                        "Archived".equals(status) ? "Archived" : "Ready to Archive"
                    };
                    historyModel.addRow(row);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading history: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    

}
