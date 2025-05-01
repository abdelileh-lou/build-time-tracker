package com.time.time_traking.controller;

import com.time.time_traking.model.*;
import com.time.time_traking.service.EmployeeService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return new ResponseEntity<>(employeeService.getAllEmployees(), HttpStatus.OK);
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        if (employee != null) {
            return new ResponseEntity<>(employee, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/employee")
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
        // Department should be set in the request body
        Employee savedEmployee = employeeService.addEmployee(employee);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    @PostMapping("/manager")
    public ResponseEntity<Manager> addManager(@RequestBody Manager manager) {
        // Department should be set in the request body
        Manager savedManager = employeeService.addManager(manager);
        return new ResponseEntity<>(savedManager, HttpStatus.CREATED);
    }

    @PostMapping("/chef-service")
    public ResponseEntity<ChefService> addChefService(@RequestBody ChefService chefService) {
        // Department should be set in the request body
        ChefService savedChef = employeeService.addChefService(chefService);
        return new ResponseEntity<>(savedChef, HttpStatus.CREATED);
    }

    @DeleteMapping("/employee/{id}")
    public ResponseEntity<Employee> deleteEmployee(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        if (employee != null) {
            employeeService.deleteEmployee(id);
            return new ResponseEntity<>(employee, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/employees/role/{role}")
    public ResponseEntity<List<Employee>> getEmployeesByRole(@PathVariable Role role) {
        return new ResponseEntity<>(employeeService.getEmployeesByRole(role), HttpStatus.OK);
    }


    //delete
    @GetMapping("/employees/department/{department}")
    public ResponseEntity<List<Employee>> getEmployeesByDepartment(@PathVariable String department) {
        return new ResponseEntity<>(employeeService.getEmployeesByDepartment(department), HttpStatus.OK);
    }



    @GetMapping("/AllEmployees")
    public  ResponseEntity<List<Employee>> getAllEmployeesExceptChefAndManger() {
        List<Employee> employees = employeeService.getAllEmployeesExceptChefAndManger();
        return ResponseEntity.ok(employees);
    }




  //new
    @GetMapping("planning/employee/{id}")

    public ResponseEntity<Planning> getPlanning(@PathVariable Long id) {
        Planning planning = employeeService.getPlanning(id);
        if (planning != null) {
            System.out.println(planning);
            return new ResponseEntity<>(planning, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }





}