package com.time.time_traking.repository;

import com.time.time_traking.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByRole(Role role);

    // EmployeeRepository.java
    @Query("SELECT e FROM Employee e WHERE e.department = :department AND TYPE(e) = Manager")
    List<Manager> findManagersByDepartment(@Param("department") String department);

    @Query("SELECT e FROM Employee e WHERE e.department = :department")
    List<Employee> findByDepartment(String department);


    //delete
   @Query("SELECT e FROM Employee e WHERE TYPE(e) = ChefService")
    List<ChefService> findAllChefServices();



}