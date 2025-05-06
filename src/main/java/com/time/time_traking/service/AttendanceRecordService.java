package com.time.time_traking.service;

import com.time.time_traking.model.AttendanceRecord;
import com.time.time_traking.model.Employee;
import com.time.time_traking.repository.AttendanceRecordRepository;
import com.time.time_traking.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AttendanceRecordService {

    private final EmployeeRepository employeeRepository;
    private final AttendanceRecordRepository attendanceRecordRepository;

    public AttendanceRecordService(AttendanceRecordRepository attendanceRecordRepository ,  EmployeeRepository employeeRepository) {
        this.attendanceRecordRepository = attendanceRecordRepository;
        this.employeeRepository = employeeRepository;
    }

    public AttendanceRecord recordAttendance(Long employeeId, String status) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        AttendanceRecord record = new AttendanceRecord();
        record.setEmployee(employee);
        record.setTimestamp(LocalDateTime.now());
        record.setStatus(status);

        return attendanceRecordRepository.save(record);
    }

    public List<AttendanceRecord> getTodayAttendanceByService(String service) {
        return attendanceRecordRepository.findTodayByService(
                service,
                LocalDate.now()
        );
    }
}
