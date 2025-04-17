package com.time.time_traking.repository;


import com.time.time_traking.model.Planning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanningRepository  extends JpaRepository<Planning, Long> {

    List<Planning> findByDepartment(String department);
    List<Planning> findByEmployeeId(Long employeeId);
}
