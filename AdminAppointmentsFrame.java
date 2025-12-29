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

public class AdminAppointmentsFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private final AppointmentService appointmentService;
    private JComboBox<String> statusFilter;

    public AdminAppointmentsFrame() {
        this.appointmentService = new AppointmentService();
        initializeComponents();
    }

    private void initializeComponents() {
        setTitle("View All Appointments");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 247, 250));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Header
        JLabel titleLabel = new JLabel("All Appointments - View Only");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setForeground(new Color(33, 37, 41));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(new Color(245, 247, 250));
        
        JLabel filterLabel = new JLabel("Filter by Status:");
        filterLabel.setFont(new Font("Roboto", Font.BOLD, 14));
        
        String[] statuses = {"All", "Pending", "Assigned", "Working", "Completed", "Cancelled"};
        statusFilter = new JComboBox<>(statuses);
        statusFilter.setFont(new Font("Open Sans", Font.PLAIN, 14));
        statusFilter.addActionListener(e -> loadAppointments());
        
        filterPanel.add(filterLabel);
        filterPanel.add(statusFilter);
        
        // Table
        String[] columns = {"ID", "Customer ID", "Problem", "Device", "Date", "Time", "Status", "Technician"};
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
        table.getTableHeader().setBackground(new Color(0, 123, 255));
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
                        case "Assigned":
                            c.setBackground(new Color(212, 237, 218));
                            c.setForeground(new Color(21, 87, 36));
                            break;
                        case "Working":
                            c.setBackground(new Color(204, 229, 255));
                            c.setForeground(new Color(4, 85, 224));
                            break;
                        case "Completed":
                            c.setBackground(new Color(195, 230, 203));
                            c.setForeground(new Color(21, 87, 36));
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

        loadAppointments();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(new Color(220, 225, 230), 1));
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(245, 247, 250));
        centerPanel.add(filterPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Control Panel
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.setBackground(new Color(245, 247, 250));
        controlPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setFont(new Font("Roboto", Font.BOLD, 14));
        refreshBtn.setPreferredSize(new Dimension(100, 35));
        refreshBtn.setBackground(new Color(0, 123, 255));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setBorder(BorderFactory.createEmptyBorder());
        refreshBtn.setFocusPainted(false);
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.addActionListener(e -> {
            loadAppointments();
            JOptionPane.showMessageDialog(this, "Appointments refreshed!", "Refresh", JOptionPane.INFORMATION_MESSAGE);
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

    private void loadAppointments() {
        model.setRowCount(0);
        try {
            List<Appointment> appointments = appointmentService.getActiveAppointments();
            String selectedStatus = (String) statusFilter.getSelectedItem();
            
            for (Appointment appointment : appointments) {
                String status = appointment.getStatus() != null ? appointment.getStatus() : "Pending";
                
                // Apply filter
                if (!"All".equals(selectedStatus) && !selectedStatus.equals(status)) {
                    continue;
                }
                
                String technicianName = appointment.getAssignedTechnicianName();
                if (technicianName == null || technicianName.trim().isEmpty()) {
                    technicianName = "Unassigned";
                }
                
                Object[] row = {
                    appointment.getId(),
                    appointment.getUserId(),
                    appointment.getIssue(),
                    appointment.getDevice(),
                    appointment.getDate(),
                    appointment.getTime(),
                    status,
                    technicianName
                };
                model.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading appointments: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
