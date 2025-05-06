package com.time.time_traking.repository;

import com.time.time_traking.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByRole(Role role);

    // EmployeeRepository.java
    @Query("SELECT e FROM Employee e WHERE e.user.services = :department AND TYPE(e) = Manager")
    List<Manager> findManagersByDepartment(@Param("department") String department);

    @Query("SELECT e FROM Employee e WHERE e.user.services = :department")
    List<Employee> findByDepartment(String department);


    //delete
   @Query("SELECT e FROM Employee e WHERE TYPE(e) = ChefService")
    List<ChefService> findAllChefServices();


    List<Employee> findEmployeesByRole(Role role);


    Planning findPlanningById(Long employeeId);


    Optional<Employee> findByUser(User user);



    //Showing employees from your service only
    @Query("SELECT e FROM Employee e WHERE e.user.services = :service AND e.role = 'EMPLOYEE'")
    List<Employee> findEmployeesByServiceAndRoleEmployee(@Param("service") String service);



}