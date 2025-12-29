package com.byteaid.appointment.system;

import com.byteaid.appointment.service.DatabaseService;
import com.byteaid.appointment.ui.AuthFrame;
import com.byteaid.appointment.util.UIUtils;

import javax.swing.*;

public class ByteAidAppointmentSystem {
    
    public static void main(String[] args) {
        try {
            UIUtils.setupUIDefaults();
        } catch (Exception e) {
            System.err.println("Could not set system look and feel: " + e.getMessage());
        }

        DatabaseService.initializeDatabase();
        
        SwingUtilities.invokeLater(() -> {
            try {
                new AuthFrame();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "Failed to start application: " + e.getMessage(), 
                    "Startup Error", 
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}
