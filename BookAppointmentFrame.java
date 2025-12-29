package com.byteaid.appointment.ui;

import com.byteaid.appointment.model.Appointment;
import com.byteaid.appointment.service.AppointmentService;
import com.byteaid.appointment.util.UIConstants;
import com.byteaid.appointment.util.UIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BookAppointmentFrame extends JFrame {
    private int userId;
    private JComboBox<String> problemCombo, deviceCombo, timeCombo;
    private JTextArea descriptionArea;
    private JButton dateButton;
    private Date selectedDate;
    private AppointmentService appointmentService;

    public BookAppointmentFrame(int userId) {
        this.userId = userId;
        this.appointmentService = new AppointmentService();
        initializeComponents();
    }

    private void initializeComponents() {
        setTitle("Book New Appointment");
        setSize(800, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 247, 250));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Header with logo in upper-left (matching login page style)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 247, 250));
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // Logo panel in upper left (matching login page)
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        logoPanel.setBackground(new Color(245, 247, 250));
        
        JLabel logoLabel = new JLabel("🔧");
        logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        logoLabel.setForeground(new Color(0, 123, 255));
        
        JLabel brandLabel = new JLabel("ByteAid");
        brandLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        brandLabel.setForeground(new Color(33, 37, 41));
        
        JLabel taglineLabel = new JLabel("Device Repair & Support");
        taglineLabel.setFont(new Font("Open Sans", Font.PLAIN, 12));
        taglineLabel.setForeground(new Color(108, 117, 125));
        
        JPanel brandPanel = new JPanel();
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        brandPanel.setBackground(new Color(245, 247, 250));
        brandPanel.add(brandLabel);
        brandPanel.add(taglineLabel);
        
        logoPanel.add(logoLabel);
        logoPanel.add(Box.createHorizontalStrut(10));
        logoPanel.add(brandPanel);
        
        // Title panel in center
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(245, 247, 250));
        
        JLabel titleLabel = new JLabel("Schedule Your Appointment", JLabel.CENTER);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 26));
        titleLabel.setForeground(new Color(33, 37, 41));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Fill out the form below to book your tech support session", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Open Sans", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(108, 117, 125));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitleLabel);
        
        headerPanel.add(logoPanel, BorderLayout.WEST);
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Form card with improved layout
        JPanel card = UIUtils.createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(25, 30, 25, 30));

        // Problem Category Section (combined issue type and specific issue)
        card.add(createFormSection("Problem Category", 
            problemCombo = createStyledCombo(new String[]{
                "🖥️ Screen Repair (Hardware)",
                "🔋 Battery Replacement (Hardware)",
                "🔌 Power Issues (Hardware)",
                "⌨️ Keyboard/Mouse Problems (Hardware)",
                "🔊 Audio Issues (Hardware)",
                "🦠 Virus Removal (Software)",
                "💾 Data Recovery (Software)",
                "💻 Operating System Issues (Software)",
                "📦 Software Installation (Software)",
                "⚙️ System Setup (Software)",
                "🌐 Internet Connection (Network)",
                "📡 WiFi Problems (Network)",
                "📶 Network Setup (Network)",
                "🔒 Security Issues (Network)",
                "📱 Mobile Device Sync (Other)",
                "💾 Backup & Recovery (Other)",
                "🎮 Gaming Issues (Other)",
                "❓ Other Problem"
            })));
        card.add(Box.createVerticalStrut(15));

        // Device Section with icons
        card.add(createFormSection("Device Type", 
            deviceCombo = createStyledCombo(new String[]{
                "💻 Laptop", 
                "🖥️ Desktop", 
                "📱 Smartphone", 
                "📱 Tablet", 
                "🔧 Other"
            })));
        card.add(Box.createVerticalStrut(15));

        // Description Section with larger text area
        JPanel descSection = new JPanel(new BorderLayout());
        descSection.setBackground(UIConstants.CARD_COLOR);
        
        JLabel descLabel = createStyledLabel("Problem Description");
        descSection.add(descLabel, BorderLayout.NORTH);
        
        descriptionArea = new JTextArea(5, 40);
        descriptionArea.setFont(new Font("Open Sans", Font.PLAIN, 14));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBorder(new EmptyBorder(12, 15, 12, 15));
        descriptionArea.setBackground(Color.WHITE);
        descriptionArea.setForeground(new Color(33, 37, 41));
        
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(206, 212, 218), 1),
            BorderFactory.createEmptyBorder()
        ));
        scrollPane.setPreferredSize(new Dimension(500, 110));
        
        descSection.add(Box.createVerticalStrut(8), BorderLayout.CENTER);
        descSection.add(scrollPane, BorderLayout.SOUTH);
        
        card.add(descSection);
        card.add(Box.createVerticalStrut(18));

        // Date and Time Section
        JPanel dateTimePanel = new JPanel(new GridLayout(1, 2, 20, 0));
        dateTimePanel.setBackground(UIConstants.CARD_COLOR);
        
        // Date Section with calendar button
        JPanel dateSection = new JPanel(new BorderLayout());
        dateSection.setBackground(UIConstants.CARD_COLOR);
        
        JLabel dateLabel = createStyledLabel("Date");
        dateSection.add(dateLabel, BorderLayout.NORTH);
        dateSection.add(Box.createVerticalStrut(8), BorderLayout.CENTER);
        
        // Initialize selected date to tomorrow
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DAY_OF_MONTH, 1);
        selectedDate = tomorrow.getTime();
        
        // Calendar date button
        dateButton = new JButton();
        updateDateButtonText();
        dateButton.setFont(new Font("Open Sans", Font.PLAIN, 14));
        dateButton.setPreferredSize(new Dimension(260, 42));
        dateButton.setBackground(Color.WHITE);
        dateButton.setForeground(new Color(33, 37, 41));
        dateButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        dateButton.setHorizontalAlignment(SwingConstants.LEFT);
        dateButton.setFocusPainted(false);
        dateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        dateButton.addActionListener(e -> showCalendarDialog());
        
        dateSection.add(dateButton, BorderLayout.SOUTH);
        
        // Time Section with AM/PM
        JPanel timeSection = new JPanel(new BorderLayout());
        timeSection.setBackground(UIConstants.CARD_COLOR);
        
        JLabel timeLabel = createStyledLabel("Time");
        timeSection.add(timeLabel, BorderLayout.NORTH);
        timeSection.add(Box.createVerticalStrut(8), BorderLayout.CENTER);
        
        String[] timeSlots = {
            "🕘 9:00 AM", "🕘 9:30 AM", "🕙 10:00 AM", "🕙 10:30 AM", "🕚 11:00 AM", "🕚 11:30 AM",
            "🕛 12:00 PM", "🕧 12:30 PM", "🕐 1:00 PM", "🕜 1:30 PM", "🕑 2:00 PM", "🕝 2:30 PM",
            "🕒 3:00 PM", "🕞 3:30 PM", "🕓 4:00 PM", "🕟 4:30 PM", "🕔 5:00 PM", "🕠 5:30 PM"
        };
        timeCombo = createStyledCombo(timeSlots);
        timeSection.add(timeCombo, BorderLayout.SOUTH);
        
        dateTimePanel.add(dateSection);
        dateTimePanel.add(timeSection);
        
        card.add(dateTimePanel);
        card.add(Box.createVerticalStrut(20));

        // Buttons with improved styling
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(UIConstants.CARD_COLOR);
        
        JButton cancelBtn = createStyledButton("Cancel", new Color(149, 165, 166));
        cancelBtn.addActionListener(e -> dispose());
        
        JButton bookBtn = createStyledButton("Book Appointment", UIConstants.PRIMARY_COLOR);
        bookBtn.addActionListener(e -> bookAppointment());
        
        buttonPanel.add(cancelBtn);
        buttonPanel.add(bookBtn);
        card.add(buttonPanel);

        mainPanel.add(card, BorderLayout.CENTER);
        add(mainPanel);
        setVisible(true);
    }

    private JPanel createFormSection(String labelText, JComponent component) {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(UIConstants.CARD_COLOR);
        
        JLabel label = createStyledLabel(labelText);
        section.add(label, BorderLayout.NORTH);
        section.add(Box.createVerticalStrut(8), BorderLayout.CENTER);
        section.add(component, BorderLayout.SOUTH);
        
        return section;
    }
    
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Roboto", Font.BOLD, 14));
        label.setForeground(new Color(52, 58, 64));
        return label;
    }
    
    private JComboBox<String> createStyledCombo(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setFont(new Font("Open Sans", Font.PLAIN, 14));
        combo.setPreferredSize(new Dimension(260, 42));
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        return combo;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Roboto", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(160, 45));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void bookAppointment() {
        if (descriptionArea.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please provide a description of the issue.", "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String problemCategory = cleanText((String) problemCombo.getSelectedItem());
        String[] parts = problemCategory.split(" \\(");
        String issue = parts[0].trim();
        String issueType = parts.length > 1 ? parts[1].replace(")", "").trim() : "Other";
        String device = cleanText((String) deviceCombo.getSelectedItem());
        String description = descriptionArea.getText().trim();
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(selectedDate);
        String time = convertTo24Hour(cleanText((String) timeCombo.getSelectedItem()));

        try {
            Appointment appointment = new Appointment(userId, issueType, issue, device, description, date, time);
            if (appointmentService.bookAppointment(appointment)) {
                SimpleDateFormat displayFormat = new SimpleDateFormat("MMM dd, yyyy (EEEE)");
                String displayDate = displayFormat.format(selectedDate);
                
                JOptionPane.showMessageDialog(this, 
                    "Appointment booked successfully!\n\nDate: " + displayDate + "\nTime: " + cleanText((String) timeCombo.getSelectedItem()) + 
                    "\n\nYou will receive a confirmation email shortly.", 
                    "Booking Confirmed", 
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error booking appointment: " + ex.getMessage(), "Booking Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateDateButtonText() {
        SimpleDateFormat format = new SimpleDateFormat("📅 MMM dd, yyyy (EEEE)");
        dateButton.setText(format.format(selectedDate));
    }
    
    private void showCalendarDialog() {
        JDialog calendarDialog = new JDialog(this, "Select Date", true);
        calendarDialog.setSize(350, 300);
        calendarDialog.setLocationRelativeTo(this);
        calendarDialog.setLayout(new BorderLayout());
        
        // Create calendar panel
        JPanel calendarPanel = createCalendarPanel(calendarDialog);
        calendarDialog.add(calendarPanel, BorderLayout.CENTER);
        
        calendarDialog.setVisible(true);
    }
    
    private JPanel createCalendarPanel(JDialog dialog) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Month/Year header
        Calendar cal = Calendar.getInstance();
        cal.setTime(selectedDate);
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel monthLabel = new JLabel(new SimpleDateFormat("MMMM yyyy").format(selectedDate), JLabel.CENTER);
        monthLabel.setFont(new Font("Roboto", Font.BOLD, 16));
        monthLabel.setForeground(new Color(33, 37, 41));
        
        headerPanel.add(monthLabel, BorderLayout.CENTER);
        panel.add(headerPanel, BorderLayout.NORTH);
        
        // Calendar grid
        JPanel gridPanel = new JPanel(new GridLayout(7, 7, 2, 2));
        gridPanel.setBackground(Color.WHITE);
        gridPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        // Day headers
        String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : dayNames) {
            JLabel dayLabel = new JLabel(day, JLabel.CENTER);
            dayLabel.setFont(new Font("Open Sans", Font.BOLD, 12));
            dayLabel.setForeground(new Color(108, 117, 125));
            gridPanel.add(dayLabel);
        }
        
        // Calendar days
        Calendar monthCal = Calendar.getInstance();
        monthCal.setTime(selectedDate);
        monthCal.set(Calendar.DAY_OF_MONTH, 1);
        
        int firstDayOfWeek = monthCal.get(Calendar.DAY_OF_WEEK) - 1;
        int daysInMonth = monthCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        Calendar minDate = Calendar.getInstance();
        minDate.add(Calendar.DAY_OF_MONTH, 1);
        
        // Empty cells before first day
        for (int i = 0; i < firstDayOfWeek; i++) {
            gridPanel.add(new JLabel());
        }
        
        // Days of month
        for (int day = 1; day <= daysInMonth; day++) {
            Calendar dayDate = Calendar.getInstance();
            dayDate.setTime(selectedDate);
            dayDate.set(Calendar.DAY_OF_MONTH, day);
            
            JButton dayButton = new JButton(String.valueOf(day));
            dayButton.setFont(new Font("Open Sans", Font.PLAIN, 12));
            dayButton.setPreferredSize(new Dimension(35, 35));
            dayButton.setBorder(BorderFactory.createEmptyBorder());
            dayButton.setFocusPainted(false);
            dayButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            if (dayDate.before(minDate)) {
                dayButton.setEnabled(false);
                dayButton.setForeground(new Color(200, 200, 200));
                dayButton.setBackground(Color.WHITE);
            } else {
                dayButton.setForeground(new Color(33, 37, 41));
                dayButton.setBackground(Color.WHITE);
                
                if (dayDate.get(Calendar.DAY_OF_MONTH) == cal.get(Calendar.DAY_OF_MONTH)) {
                    dayButton.setBackground(new Color(0, 123, 255));
                    dayButton.setForeground(Color.WHITE);
                }
                
                final int selectedDay = day;
                dayButton.addActionListener(e -> {
                    Calendar newDate = Calendar.getInstance();
                    newDate.setTime(selectedDate);
                    newDate.set(Calendar.DAY_OF_MONTH, selectedDay);
                    selectedDate = newDate.getTime();
                    updateDateButtonText();
                    dialog.dispose();
                });
            }
            
            gridPanel.add(dayButton);
        }
        
        panel.add(gridPanel, BorderLayout.CENTER);
        
        // Cancel button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Roboto", Font.BOLD, 12));
        cancelButton.setPreferredSize(new Dimension(80, 35));
        cancelButton.setBackground(new Color(149, 165, 166));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBorder(BorderFactory.createEmptyBorder());
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(e -> dialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private String cleanText(String text) {
        if (text == null) return "";
        // Remove emojis and extra spaces
        return text.replaceAll("[^\\p{L}\\p{N}\\p{P}\\p{Z}]", "").trim();
    }
    
    private String convertTo24Hour(String time12Hour) {
        try {
            SimpleDateFormat input = new SimpleDateFormat("h:mm a");
            SimpleDateFormat output = new SimpleDateFormat("HH:mm");
            Date date = input.parse(time12Hour);
            return output.format(date);
        } catch (Exception e) {
            return "09:00";
        }
    }
}
