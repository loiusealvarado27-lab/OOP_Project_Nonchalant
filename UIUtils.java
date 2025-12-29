package com.byteaid.appointment.util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class UIUtils {
    
    public static void setupUIDefaults() {
        UIManager.put("Button.background", UIConstants.PRIMARY_COLOR);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.font", UIConstants.BUTTON_FONT);
        UIManager.put("Panel.background", UIConstants.BACKGROUND_COLOR);
        UIManager.put("Label.foreground", UIConstants.TEXT_COLOR);
        UIManager.put("Label.font", UIConstants.TEXT_FONT);
    }
    
    public static JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(UIConstants.BUTTON_FONT);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    public static JPanel createCard() {
        JPanel card = new JPanel();
        card.setBackground(UIConstants.CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        return card;
    }
    
    public static JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(UIConstants.TEXT_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        return field;
    }
    
    public static JPasswordField createStyledPasswordField(int columns) {
        JPasswordField field = new JPasswordField(columns);
        field.setFont(UIConstants.TEXT_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        return field;
    }
}
