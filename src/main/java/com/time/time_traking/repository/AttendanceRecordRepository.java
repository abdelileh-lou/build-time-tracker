package com.time.time_traking.repository;

import com.time.time_traking.model.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {

    List<AttendanceRecord> findByEmployeeId(Long employeeId);
    List<AttendanceRecord> findByAttendanceTypeId(long attendanceType);
}
