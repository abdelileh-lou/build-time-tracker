package com.time.time_traking.service;

import com.time.time_traking.controller.AttendanceWebSocketController;
import com.time.time_traking.model.AttendanceRecord;
import com.time.time_traking.model.Employee;
import com.time.time_traking.model.AttendanceType;
import com.time.time_traking.repository.AttendanceRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRecordRepository attendanceRecordRepository;

    @Autowired
    private AttendanceWebSocketController webSocketController;

    public AttendanceRecord recordAttendance(Employee employee, AttendanceType attendanceType) {
        AttendanceRecord record = new AttendanceRecord();
        record.setEmployee(employee);
        record.setAttendanceType(attendanceType);
        record.setTimestamp(LocalDateTime.now());
        record.setStatus("PRESENT"); // or determine based on time

        AttendanceRecord savedRecord = attendanceRecordRepository.save(record);

        // Notify via WebSocket
        webSocketController.notifyNewAttendance(savedRecord);

        return savedRecord;
    }

    public List<AttendanceRecord> getEmployeeAttendance(Long employeeId) {
        return attendanceRecordRepository.findByEmployeeId(employeeId);
    }

    public List<AttendanceRecord> getAllAttendance() {
        return attendanceRecordRepository.findAll();
    }

    // Add other methods as needed
}