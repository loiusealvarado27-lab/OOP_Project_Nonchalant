package com.byteaid.appointment.ui;

import com.byteaid.appointment.model.Appointment;
import com.byteaid.appointment.service.AppointmentService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ViewAppointmentsFrame extends JFrame {
    private int userId;
    private JTable table;
    private DefaultTableModel model;
    private AppointmentService appointmentService;

    public ViewAppointmentsFrame(int userId) {
        this.userId = userId;
        this.appointmentService = new AppointmentService();
        initializeComponents();
    }

    private void initializeComponents() {
        setTitle("My Appointments");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 247, 250));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Header with ByteAid branding
        JPanel headerPanel = createHeader();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Enhanced Table
        String[] columns = {"ID", "Problem Category", "Device", "Date", "Time", "Status", "Description"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        table = new JTable(model);
        table.setFont(new Font("Open Sans", Font.PLAIN, 13));
        table.setRowHeight(35);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(0, 123, 255));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        
        // Set column widths
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);  // ID
        columnModel.getColumn(1).setPreferredWidth(200); // Problem Category
        columnModel.getColumn(2).setPreferredWidth(120); // Device
        columnModel.getColumn(3).setPreferredWidth(100); // Date
        columnModel.getColumn(4).setPreferredWidth(80);  // Time
        columnModel.getColumn(5).setPreferredWidth(100); // Status
        columnModel.getColumn(6).setPreferredWidth(250); // Description
        
        // Enhanced cell renderer for status
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
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
                        case "Cancelled":
                            c.setBackground(new Color(248, 215, 218));
                            c.setForeground(new Color(114, 28, 36));
                            break;
                        default:
                            c.setBackground(Color.WHITE);
                            c.setForeground(Color.BLACK);
                    }
                } else {
                    c.setForeground(Color.WHITE);
                }
                return c;
            }
        });
        
        loadAppointments();
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 225, 230), 1),
            BorderFactory.createEmptyBorder()
        ));
        scrollPane.getViewport().setBackground(Color.WHITE);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Enhanced button panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
        
        // Ensure window stays visible
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        toFront();
        requestFocus();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(245, 247, 250));
        header.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        JLabel titleLabel = new JLabel("My Appointments");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setForeground(new Color(33, 37, 41));
        
        JLabel subtitleLabel = new JLabel("View and track your device repair appointments");
        subtitleLabel.setFont(new Font("Open Sans", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(108, 117, 125));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(245, 247, 250));
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitleLabel);
        
        header.add(titlePanel, BorderLayout.WEST);
        return header;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(new Color(245, 247, 250));
        panel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        JButton editBtn = createStyledButton("Edit Appointment", new Color(0, 123, 255));
        editBtn.addActionListener(e -> editSelectedAppointment());
        
        JButton refreshBtn = createStyledButton("Refresh", new Color(40, 167, 69));
        refreshBtn.addActionListener(e -> {
            loadAppointments();
            JOptionPane.showMessageDialog(this, "Appointments refreshed successfully!", "Refresh", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JButton closeBtn = createStyledButton("Close", new Color(108, 117, 125));
        closeBtn.addActionListener(e -> dispose());
        
        panel.add(editBtn);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(refreshBtn);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(closeBtn);
        
        return panel;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Roboto", Font.BOLD, 13));
        button.setPreferredSize(new Dimension(text.length() > 10 ? 140 : 100, 35));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private void editSelectedAppointment() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String idStr = model.getValueAt(selectedRow, 0).toString();
        String status = model.getValueAt(selectedRow, 5).toString();
        
        if (idStr.equals("--")) {
            JOptionPane.showMessageDialog(this, "No valid appointment selected.", "Invalid Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Check if appointment can be edited
        if (!"Pending".equals(status) && !"Confirmed".equals(status)) {
            JOptionPane.showMessageDialog(this, "Appointments can only be edited when status is Pending or Confirmed.", "Cannot Edit", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int appointmentId = Integer.parseInt(idStr);
            showEditDialog(appointmentId, selectedRow);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid appointment ID.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showEditDialog(int appointmentId, int selectedRow) {
        JDialog editDialog = new JDialog(this, "Edit Appointment", true);
        editDialog.setSize(500, 400);
        editDialog.setLocationRelativeTo(this);
        editDialog.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);
        
        // Problem Category
        JLabel problemLabel = new JLabel("Problem Category:");
        problemLabel.setFont(new Font("Roboto", Font.BOLD, 14));
        String[] problems = {
            "🖥️ Screen Repair (Hardware)", "🔋 Battery Replacement (Hardware)", "🔌 Power Issues (Hardware)",
            "⌨️ Keyboard/Mouse Problems (Hardware)", "🔊 Audio Issues (Hardware)", "🦠 Virus Removal (Software)",
            "💾 Data Recovery (Software)", "💻 Operating System Issues (Software)", "📦 Software Installation (Software)",
            "⚙️ System Setup (Software)", "🌐 Internet Connection (Network)", "📡 WiFi Problems (Network)",
            "📶 Network Setup (Network)", "🔒 Security Issues (Network)", "📱 Mobile Device Sync (Other)",
            "💾 Backup & Recovery (Other)", "🎮 Gaming Issues (Other)", "❓ Other Problem"
        };
        JComboBox<String> problemCombo = new JComboBox<>(problems);
        problemCombo.setFont(new Font("Open Sans", Font.PLAIN, 14));
        
        // Device Type
        JLabel deviceLabel = new JLabel("Device Type:");
        deviceLabel.setFont(new Font("Roboto", Font.BOLD, 14));
        String[] devices = {"💻 Laptop", "🖥️ Desktop", "📱 Smartphone", "📱 Tablet", "🔧 Other"};
        JComboBox<String> deviceCombo = new JComboBox<>(devices);
        deviceCombo.setFont(new Font("Open Sans", Font.PLAIN, 14));
        
        // Description
        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(new Font("Roboto", Font.BOLD, 14));
        JTextArea descArea = new JTextArea(4, 30);
        descArea.setFont(new Font("Open Sans", Font.PLAIN, 14));
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descArea);
        
        // Get current values and set them
        try {
            List<Appointment> appointments = appointmentService.getUserAppointments(userId);
            for (Appointment apt : appointments) {
                if (apt.getId() == appointmentId) {
                    // Set current values

                    for (int i = 0; i < problems.length; i++) {
                        if (problems[i].contains(apt.getIssue())) {
                            problemCombo.setSelectedIndex(i);
                            break;
                        }
                    }
                    
                    for (int i = 0; i < devices.length; i++) {
                        if (devices[i].contains(apt.getDevice())) {
                            deviceCombo.setSelectedIndex(i);
                            break;
                        }
                    }
                    
                    descArea.setText(apt.getDescription());
                    break;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(editDialog, "Error loading appointment details.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        formPanel.add(problemLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(problemCombo);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(deviceLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(deviceCombo);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(descLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(descScroll);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton saveBtn = new JButton("Save Changes");
        saveBtn.setBackground(new Color(40, 167, 69));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        saveBtn.setFocusPainted(false);
        saveBtn.addActionListener(e -> {
            if (saveAppointmentChanges(appointmentId, problemCombo, deviceCombo, descArea)) {
                editDialog.dispose();
                loadAppointments();
            }
        });
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(new Color(108, 117, 125));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        cancelBtn.setFocusPainted(false);
        cancelBtn.addActionListener(e -> editDialog.dispose());
        
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        
        editDialog.add(formPanel, BorderLayout.CENTER);
        editDialog.add(buttonPanel, BorderLayout.SOUTH);
        editDialog.setVisible(true);
    }
    
    private boolean saveAppointmentChanges(int appointmentId, JComboBox<String> problemCombo, JComboBox<String> deviceCombo, JTextArea descArea) {
        try {
            String problemCategory = (String) problemCombo.getSelectedItem();
            String[] parts = problemCategory.split(" \\(");
            String issue = parts[0].replaceAll("[^\\p{L}\\p{N}\\p{P}\\p{Z}]", "").trim();
            String issueType = parts.length > 1 ? parts[1].replace(")", "").trim() : "Other";
            
            String device = ((String) deviceCombo.getSelectedItem()).replaceAll("[^\\p{L}\\p{N}\\p{P}\\p{Z}]", "").trim();
            String description = descArea.getText().trim();
            
            if (appointmentService.updateAppointmentDetails(appointmentId, issueType, issue, device, description)) {
                JOptionPane.showMessageDialog(this, "Appointment updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update appointment.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void loadAppointments() {
        model.setRowCount(0);
        try {
            List<Appointment> appointments = appointmentService.getUserAppointments(userId);
            if (appointments.isEmpty()) {
                // Show message when no appointments found
                Object[] row = {"--", "No appointments found", "", "", "", "", ""};
                model.addRow(row);
            } else {
                for (Appointment appointment : appointments) {
                    String problemCategory = appointment.getIssue() + " (" + appointment.getIssueType() + ")";
                    String description = appointment.getDescription();
                    if (description != null && description.length() > 50) {
                        description = description.substring(0, 47) + "...";
                    }
                    
                    Object[] row = {
                        String.valueOf(appointment.getId()),
                        problemCategory,
                        appointment.getDevice() != null ? appointment.getDevice() : "Unknown",
                        appointment.getDate() != null ? appointment.getDate() : "N/A",
                        appointment.getTime() != null ? appointment.getTime() : "N/A",
                        appointment.getStatus() != null ? appointment.getStatus() : "Pending",
                        description != null ? description : "No description"
                    };
                    model.addRow(row);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error loading appointments: " + ex.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
