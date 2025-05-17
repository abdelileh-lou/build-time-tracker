package com.time.time_traking.controller;


import com.time.time_traking.DTO.AttendanceRecordDTO;
import com.time.time_traking.DTO.AttendanceRecordRequest;
import com.time.time_traking.model.AttendanceRecord;
import com.time.time_traking.model.Employee;
import com.time.time_traking.repository.AttendanceRecordRepository;
import com.time.time_traking.service.AttendanceRecordService;
import com.time.time_traking.service.EmployeeService;
import org.springframework.boot.autoconfigure.batch.BatchTransactionManager;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceRecordController {



    private static final long QR_CODE_VALID_SECONDS = 300;
    private EmployeeService employeeService;
    private AttendanceRecordService attendanceRecordService;
    private AttendanceRecordRepository attendanceRecordRepository;

    public AttendanceRecordController(AttendanceRecordService attendanceRecordService , EmployeeService employeeService , AttendanceRecordRepository attendanceRecordRepository) {
        this.attendanceRecordService = attendanceRecordService;
        this.employeeService = employeeService;
        this.attendanceRecordRepository = attendanceRecordRepository;
    }

//    @PostMapping("/record")
//    public AttendanceRecord recordAttendance(@RequestBody AttendanceRecordRequest request) {
//        Employee employee = employeeService.findById(request.getEmployeeId());
//
//        AttendanceRecord record = new AttendanceRecord();
//        record.setEmployee(employee);
//        record.setStatus(request.getStatus());
//        record.setTimestamp(LocalDateTime.now());
//
//        record = attendanceRecordService.save(record);
//        return  record;
//    }
@PostMapping("/record")
public AttendanceRecord recordAttendance(@RequestBody AttendanceRecordRequest request) {
    // Add QR code validation logic here
    if (request.getQrCode() != null) {
        // Validate QR code format and expiration
        if (!request.getQrCode().startsWith("TIMETRACK-") ||
                !isValidQrCode(request.getQrCode())) {
            throw new RuntimeException("QR code invalide");
        }


    }


    Employee employee = employeeService.findById(request.getEmployeeId());

    AttendanceRecord record = new AttendanceRecord();
    record.setEmployee(employee);
    record.setStatus(request.getStatus());
    record.setTimestamp(LocalDateTime.now());
    record.setMethod(request.getQrCode() != null ? "QR_CODE" : "FACIAL");

    return attendanceRecordService.save(record);
}
    private boolean isValidQrCode(String qrCode) {
        // Check format: TIMETRACK-<timestamp>-<random-string>
        String[] parts = qrCode.split("-");
        if (parts.length < 3 || !parts[0].equals("TIMETRACK")) {
            return false;
        }

        // Check expiration time
        long qrTimestamp = Long.parseLong(parts[1]);
        long currentTime = System.currentTimeMillis() / 1000;

        return (currentTime - qrTimestamp) <= QR_CODE_VALID_SECONDS;
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

    @PostMapping("/report-to-chef")
    public ResponseEntity<List<AttendanceRecordDTO>> reportToChef() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        List<AttendanceRecord> records = attendanceRecordRepository.findByTimestampBetween(startOfDay, endOfDay);

        List<AttendanceRecordDTO> dtos = records.stream()
                .map(record -> {
                    AttendanceRecordDTO dto = new AttendanceRecordDTO(
                            record.getId(),
                            record.getEmployee().getId(),
                            record.getEmployee().getName(),
                            record.getTimestamp(),
                            record.getStatus(),
                            record.isNotifiedManager(),
                            record.isReportedChef());

                    // Mark the record as reported to chef
                    record.setReportedChef(true);
                    attendanceRecordService.save(record);

                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }


    @PostMapping("/qr-code")
    public ResponseEntity<?> generateQrCode() {

        String qrData = "TIMETRACK-" + UUID.randomUUID().toString() + "-" + System.currentTimeMillis();

        // You might want to store this in database with expiration time
        return ResponseEntity.ok().body(Map.of(
                "qrCode", qrData,
                "expiresAt", LocalDateTime.now().plusMinutes(5)
        ));
    }


    @GetMapping("/history")
    public ResponseEntity<List<AttendanceRecordDTO>> getAttendanceHistory(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) String service,
            @RequestParam(required = false) String status) {

        List<AttendanceRecord> records = attendanceRecordService.getAttendanceHistory(startDate, endDate, employeeId, service, status);

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