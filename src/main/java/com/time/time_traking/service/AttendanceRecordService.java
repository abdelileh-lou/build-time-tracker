package com.time.time_traking.service;

import com.time.time_traking.model.AttendanceRecord;
import com.time.time_traking.model.Employee;
import com.time.time_traking.repository.AttendanceRecordRepository;
import com.time.time_traking.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class AttendanceRecordService {

    private final EmployeeRepository employeeRepository;
    private final AttendanceRecordRepository attendanceRecordRepository;

    public AttendanceRecordService(AttendanceRecordRepository attendanceRecordRepository ,  EmployeeRepository employeeRepository) {
        this.attendanceRecordRepository = attendanceRecordRepository;
        this.employeeRepository = employeeRepository;
    }

    public AttendanceRecord recordAttendance(Long employeeId, String status , LocalDateTime timestamp) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        AttendanceRecord record = new AttendanceRecord();
        record.setEmployee(employee);
        record.setTimestamp(LocalDateTime.now());
        record.setStatus(status);
        record.setTimestamp(timestamp);

        return attendanceRecordRepository.save(record);
    }

    public List<AttendanceRecord> getTodayAttendanceByService(String service) {
        return attendanceRecordRepository.findTodayByService(
                service,
                LocalDate.now()
        );



    }

    public AttendanceRecord save(AttendanceRecord record) {
        return attendanceRecordRepository.save(record);
    }

    public AttendanceRecord findByEmployeeId(Long employeeId) {
        return attendanceRecordRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("Attendance record not found for employee ID: " + employeeId));
    }


    public List<AttendanceRecord> getAttendanceHistory(LocalDate startDate, LocalDate endDate, Long employeeId, String service, String status) {
        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = (endDate != null) ? endDate.atTime(LocalTime.MAX) : null;
        return attendanceRecordRepository.findHistory(startDateTime, endDateTime, employeeId, service, status);
    }


    //new to delete
    // Add this method
    public List<AttendanceRecord> getAttendanceRecordsByEmployeeId(Long employeeId) {
        return attendanceRecordRepository.findByEmployeeIdOrderByTimestampDesc(employeeId);
    }

}