package com.time.time_traking.repository;

import com.time.time_traking.model.AttendanceConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceConfigurationRepository extends JpaRepository<AttendanceConfiguration, Long> {
}
