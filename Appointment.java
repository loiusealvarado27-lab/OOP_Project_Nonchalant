package com.byteaid.appointment.model;

public class Appointment {
    private int id;
    private int userId;
    private String issueType;
    private String issue;
    private String device;
    private String description;
    private String date;
    private String time;
    private String status;
    private Integer assignedTechnicianId;

    public Appointment() {}

    public Appointment(int userId, String issueType, String issue, String device, 
                      String description, String date, String time) {
        this.userId = userId;
        this.issueType = issueType;
        this.issue = issue;
        this.device = device;
        this.description = description;
        this.date = date;
        this.time = time;
        this.status = "Pending";
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getIssueType() { return issueType; }
    public void setIssueType(String issueType) { this.issueType = issueType; }

    public String getIssue() { return issue; }
    public void setIssue(String issue) { this.issue = issue; }

    public String getDevice() { return device; }
    public void setDevice(String device) { this.device = device; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getAssignedTechnicianId() { return assignedTechnicianId; }
    public void setAssignedTechnicianId(Integer assignedTechnicianId) { this.assignedTechnicianId = assignedTechnicianId; }
    
    private String assignedTechnicianName;
    
    public String getAssignedTechnicianName() { return assignedTechnicianName; }
    public void setAssignedTechnicianName(String assignedTechnicianName) { this.assignedTechnicianName = assignedTechnicianName; }
}
