package com.byteaid.appointment.service;

import com.byteaid.appointment.model.Appointment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentService {

    public boolean bookAppointment(Appointment appointment) throws SQLException {
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pst = conn.prepareStatement(
                 "INSERT INTO appointments(user_id, issue_type, issue, device, description, date, time) VALUES(?,?,?,?,?,?,?)")) {
            pst.setInt(1, appointment.getUserId());
            pst.setString(2, appointment.getIssueType());
            pst.setString(3, appointment.getIssue());
            pst.setString(4, appointment.getDevice());
            pst.setString(5, appointment.getDescription());
            pst.setString(6, appointment.getDate());
            pst.setString(7, appointment.getTime());
            return pst.executeUpdate() > 0;
        }
    }

    public List<Appointment> getUserAppointments(int userId) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pst = conn.prepareStatement("SELECT * FROM appointments WHERE user_id=? ORDER BY date DESC, time DESC")) {
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
        }
        return appointments;
    }

    public List<Appointment> getAllAppointments() throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        try (Connection conn = DatabaseService.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM appointments ORDER BY date DESC, time DESC")) {
            
            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
        }
        return appointments;
    }

    public boolean updateAppointmentStatus(int appointmentId, String status) throws SQLException {
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pst = conn.prepareStatement("UPDATE appointments SET status=? WHERE id=?")) {
            pst.setString(1, status);
            pst.setInt(2, appointmentId);
            return pst.executeUpdate() > 0;
        }
    }
    
    public boolean updateAppointmentDetails(int appointmentId, String issueType, String issue, String device, String description) throws SQLException {
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pst = conn.prepareStatement(
                 "UPDATE appointments SET issue_type=?, issue=?, device=?, description=? WHERE id=? AND status IN ('Pending', 'Confirmed')")) {
            pst.setString(1, issueType);
            pst.setString(2, issue);
            pst.setString(3, device);
            pst.setString(4, description);
            pst.setInt(5, appointmentId);
            return pst.executeUpdate() > 0;
        }
    }
    
    public boolean assignTechnician(int appointmentId, String technicianName) throws SQLException {
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pst = conn.prepareStatement(
                 "UPDATE appointments SET assigned_technician_id=?, assigned_technician_name=? WHERE id=? AND status NOT IN ('Completed', 'Cancelled', 'Archived')")) {
            pst.setString(1, technicianName);
            pst.setString(2, technicianName);
            pst.setInt(3, appointmentId);
            return pst.executeUpdate() > 0;
        }
    }
    
    public boolean moveCompletedToHistory() throws SQLException {
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pst = conn.prepareStatement(
                 "UPDATE appointments SET status='Archived' WHERE status IN ('Completed', 'Cancelled')")) {
            return pst.executeUpdate() > 0;
        }
    }
    
    public boolean moveToHistory(int appointmentId) throws SQLException {
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pst = conn.prepareStatement(
                 "UPDATE appointments SET status='Archived' WHERE id=? AND status IN ('Completed', 'Cancelled')")) {
            pst.setInt(1, appointmentId);
            return pst.executeUpdate() > 0;
        }
    }
    
    public boolean moveToHistoryByStatus(int appointmentId) throws SQLException {
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pst = conn.prepareStatement(
                 "UPDATE appointments SET status='Archived' WHERE id=? AND status IN ('Completed', 'Cancelled', 'Device Picked Up')")) {
            pst.setInt(1, appointmentId);
            return pst.executeUpdate() > 0;
        }
    }
    
    public List<Appointment> getCompletedAndCancelledAppointments() throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pst = conn.prepareStatement(
                 "SELECT * FROM appointments WHERE status IN ('Completed', 'Cancelled') ORDER BY date DESC, time DESC")) {
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
        }
        return appointments;
    }
    
    public List<Appointment> getHistoryAppointments() throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pst = conn.prepareStatement(
                 "SELECT * FROM appointments WHERE status = 'Archived' ORDER BY date DESC, time DESC")) {
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
        }
        return appointments;
    }
    
    public List<Appointment> getActiveAppointments() throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pst = conn.prepareStatement(
                 "SELECT * FROM appointments WHERE status != 'Archived' ORDER BY date DESC, time DESC")) {
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
        }
        return appointments;
    }

    public int getUserAppointmentCount(int userId) throws SQLException {
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pst = conn.prepareStatement("SELECT COUNT(*) FROM appointments WHERE user_id=?")) {
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
    
    public int getPendingAppointmentCount(int userId) throws SQLException {
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pst = conn.prepareStatement("SELECT COUNT(*) FROM appointments WHERE user_id=? AND status IN ('Pending', 'Assigned', 'Working')")) {
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
    
    public int getCompletedAppointmentCount(int userId) throws SQLException {
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pst = conn.prepareStatement("SELECT COUNT(*) FROM appointments WHERE user_id=? AND status='Completed'")) {
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private Appointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setId(rs.getInt("id"));
        appointment.setUserId(rs.getInt("user_id"));
        appointment.setIssueType(rs.getString("issue_type"));
        appointment.setIssue(rs.getString("issue"));
        appointment.setDevice(rs.getString("device"));
        appointment.setDescription(rs.getString("description"));
        appointment.setDate(rs.getString("date"));
        appointment.setTime(rs.getString("time"));
        appointment.setStatus(rs.getString("status"));
        
        // Handle assigned technician safely
        try {
            String techName = rs.getString("assigned_technician_name");
            if (techName != null && !rs.wasNull()) {
                appointment.setAssignedTechnicianName(techName);
                appointment.setAssignedTechnicianId(1);
            } else {
                appointment.setAssignedTechnicianName(null);
                appointment.setAssignedTechnicianId(null);
            }
        } catch (SQLException e) {
            // Column might not exist, set defaults
            appointment.setAssignedTechnicianName(null);
            appointment.setAssignedTechnicianId(null);
        }
        
        return appointment;
    }
}
