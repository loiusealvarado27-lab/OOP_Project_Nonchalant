package com.byteaid.appointment.service;

import java.util.ArrayList;
import java.util.List;

public class TechnicianService {
    
    public List<String> getAvailableTechnicians() {
        List<String> technicians = new ArrayList<>();
        technicians.add("GG");
        technicians.add("Admin1");
        technicians.add("Admin2");
        technicians.add("Admin3");
        technicians.add("Admin4");
        return technicians;
    }
    
    public boolean isValidTechnician(String technicianName) {
        return getAvailableTechnicians().contains(technicianName);
    }
}
