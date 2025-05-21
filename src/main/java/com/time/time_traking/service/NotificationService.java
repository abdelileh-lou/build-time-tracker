package com.time.time_traking.service;

import com.time.time_traking.model.AttendanceRecord;
import com.time.time_traking.model.ChefService;
import com.time.time_traking.model.Manager;
import com.time.time_traking.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmailService emailService;




    // In NotificationService.java
    public void notifyManager(AttendanceRecord record) {
        if (record.getEmployee().getUser().getServices() == null) {
            return; // Skip if employee has no department
        }

        // Get all managers in the same department
        List<Manager> managers = employeeRepository.findManagersByDepartment(
                record.getEmployee().getUser().getServices()
        );

        for (Manager manager : managers) {
            // Send notification
            String message = String.format(
                    "Employee %s has checked in at %s",
                    record.getEmployee().getName(),
                    record.getTimestamp()
            );
            emailService.sendEmail(manager.getEmail(), "Attendance Notification", message);
        }

        record.setNotifiedManager(true);
    }
    public void generateReportForChef(AttendanceRecord record) {
        // Get all chef services
        List<ChefService> chefs = employeeRepository.findAllChefServices();

        for (ChefService chef : chefs) {
            // Generate and send report
            String report = generateAttendanceReport(record);
            emailService.sendEmail(chef.getEmail(), "Attendance Report", report);
        }

        record.setReportedChef(true);
    }

    private String generateAttendanceReport(AttendanceRecord record) {
        return String.format(
                "Attendance Report\nEmployee: %s\nDepartment: %s\nTime: %s\nType: %s\nStatus: %s",
                record.getEmployee().getName(),
                record.getEmployee().getUser().getServices(),
                record.getTimestamp(),

                record.getStatus()
        );
    }
}