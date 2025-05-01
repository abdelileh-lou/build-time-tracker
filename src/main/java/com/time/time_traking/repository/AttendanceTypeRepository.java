package com.time.time_traking.repository;

import com.time.time_traking.model.AttendanceType;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AttendanceTypeRepository extends JpaRepository<AttendanceType, Long> {


}
