package com.time.time_traking.controller;


import com.time.time_traking.model.AttendanceRecord;
import com.time.time_traking.service.AttendanceRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceRecordController {

    private AttendanceRecordService attendanceRecordService;

    public AttendanceRecordController(AttendanceRecordService attendanceRecordService) {
        this.attendanceRecordService = attendanceRecordService;
    }

    @PostMapping("/record")
    public ResponseEntity<AttendanceRecord> recordAttendance(
            @RequestParam Long employeeId,
            @RequestParam String status) {
        AttendanceRecord record = attendanceRecordService.recordAttendance(employeeId, status);
        return ResponseEntity.ok(record);
    }

    @GetMapping("/today/service/{service}")
    public ResponseEntity<List<AttendanceRecord>> getTodayAttendanceByService(
            @PathVariable String service) {
        List<AttendanceRecord> records = attendanceRecordService.getTodayAttendanceByService(service);
        return ResponseEntity.ok(records);
    }
}
