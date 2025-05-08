package com.time.time_traking.service;

import com.time.time_traking.DTO.AttendanceRecordDTO;
import com.time.time_traking.model.AttendanceRecord;
import com.time.time_traking.repository.AttendanceRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChefServices {

    private final AttendanceRecordRepository attendanceRecordRepository;

    public ChefServices(AttendanceRecordRepository attendanceRecordRepository) {
        this.attendanceRecordRepository = attendanceRecordRepository;
    }

    @Transactional
    public List<AttendanceRecordDTO> receiveAttendanceRecords(List<AttendanceRecordDTO> records) {
        // Here you can add any additional processing logic for the chef service
        // For example:
        // - Validate the records
        // - Store them in a separate table
        // - Generate reports
        // - Send notifications

        return records;
    }

    @Transactional(readOnly = true)
    public List<AttendanceRecordDTO> getAttendanceRecords() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        List<AttendanceRecord> records = attendanceRecordRepository.findByTimestampBetween(startOfDay, endOfDay);

        return records.stream()
                .map(record -> new AttendanceRecordDTO(
                        record.getId(),
                        record.getEmployee().getId(),
                        record.getEmployee().getName(),
                        record.getTimestamp(),
                        record.getStatus(),
                        record.isNotifiedManager(),
                        record.isReportedChef()))
                .collect(Collectors.toList());
    }
} 