package com.time.time_traking.repository;

import com.time.time_traking.model.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {
    @Query("SELECT ar FROM AttendanceRecord ar " +
            "WHERE ar.employee.user.services = :service " +
            "AND CAST(ar.timestamp AS LocalDate) = :date")
    List<AttendanceRecord> findTodayByService(@Param("service") String service,
                                              @Param("date") LocalDate date);



        Optional<AttendanceRecord> findByEmployeeId(Long employeeId);

    // Find attendance records by timestamp range (for today's records)
    List<AttendanceRecord> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

    // Find attendance records for specific employee by timestamp range
    List<AttendanceRecord> findByEmployeeIdAndTimestampBetween(Long employeeId, LocalDateTime start, LocalDateTime end);

}
