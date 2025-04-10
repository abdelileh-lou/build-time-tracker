package com.time.time_traking.controller;


import com.time.time_traking.model.AttendanceRecord;
import com.time.time_traking.model.AttendanceType;
import com.time.time_traking.model.Employee;
import com.time.time_traking.service.AttendanceService;
import com.time.time_traking.service.AttendanceTypeService;
import com.time.time_traking.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AttendanceTypeService attendanceTypeService;

    @PostMapping("/record")
    public ResponseEntity<?> recordAttendance( @RequestParam Long employeeId, @RequestParam Long attendanceTypeId) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        AttendanceType attendanceType = attendanceTypeService.getAttendanceTypeById(attendanceTypeId);

        if(employee == null || attendanceType == null) {
            return ResponseEntity.badRequest().body("Employee or attendance type not found");

        }

        AttendanceRecord record = attendanceService.recordAttendance(employee, attendanceType);
        return ResponseEntity.ok().body(record);

    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<AttendanceRecord>> getEmployeeAttendance(@PathVariable Long employeeId) {
        List<AttendanceRecord> records = attendanceService.getEmployeeAttendance(employeeId);
        return ResponseEntity.ok(records);
    }

    // Add endpoint for manager to view all attendance
    @GetMapping("/all")
    public ResponseEntity<List<AttendanceRecord>> getAllAttendance() {
        List<AttendanceRecord> records = attendanceService.getAllAttendance();
        return ResponseEntity.ok(records);
    }


}
