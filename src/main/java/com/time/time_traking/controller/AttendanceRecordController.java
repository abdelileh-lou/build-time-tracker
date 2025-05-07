package com.time.time_traking.controller;


import com.time.time_traking.DTO.AttendanceRecordDTO;
import com.time.time_traking.DTO.AttendanceRecordRequest;
import com.time.time_traking.model.AttendanceRecord;
import com.time.time_traking.model.Employee;
import com.time.time_traking.repository.AttendanceRecordRepository;
import com.time.time_traking.service.AttendanceRecordService;
import com.time.time_traking.service.EmployeeService;
import org.springframework.boot.autoconfigure.batch.BatchTransactionManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceRecordController {


    private EmployeeService employeeService;
    private AttendanceRecordService attendanceRecordService;
    private AttendanceRecordRepository attendanceRecordRepository;

    public AttendanceRecordController(AttendanceRecordService attendanceRecordService , EmployeeService employeeService , AttendanceRecordRepository attendanceRecordRepository) {
        this.attendanceRecordService = attendanceRecordService;
        this.employeeService = employeeService;
        this.attendanceRecordRepository = attendanceRecordRepository;
    }

    @PostMapping("/record")
    public AttendanceRecord recordAttendance(@RequestBody AttendanceRecordRequest request) {
        Employee employee = employeeService.findById(request.getEmployeeId());

        AttendanceRecord record = new AttendanceRecord();
        record.setEmployee(employee);
        record.setStatus(request.getStatus());
        record.setTimestamp(LocalDateTime.now());

        record = attendanceRecordService.save(record);
        return  record;
    }



    @GetMapping("/today/service/{service}")
    public ResponseEntity<List<AttendanceRecord>> getTodayAttendanceByService(
            @PathVariable String service) {
        List<AttendanceRecord> records = attendanceRecordService.getTodayAttendanceByService(service);
        return ResponseEntity.ok(records);
    }


    @GetMapping("record/today")
    public ResponseEntity<List<AttendanceRecordDTO>> getTodayAttendance() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        List<AttendanceRecord> records = attendanceRecordRepository.findByTimestampBetween(startOfDay, endOfDay);

        List<AttendanceRecordDTO> dtos = records.stream()
                .map(record -> new AttendanceRecordDTO(
                        record.getId(),
                        record.getEmployee().getId(),
                        record.getEmployee().getName(),
                        record.getTimestamp(),
                        record.getStatus(),
                        record.isNotifiedManager(),
                        record.isReportedChef()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);

    }

}
