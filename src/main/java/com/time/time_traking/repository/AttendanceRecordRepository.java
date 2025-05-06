package com.time.time_traking.repository;

import com.time.time_traking.model.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {
    @Query("SELECT ar FROM AttendanceRecord ar " +
            "WHERE ar.employee.user.services = :service " +
            "AND CAST(ar.timestamp AS LocalDate) = :date")
    List<AttendanceRecord> findTodayByService(@Param("service") String service,
                                              @Param("date") LocalDate date);
}
