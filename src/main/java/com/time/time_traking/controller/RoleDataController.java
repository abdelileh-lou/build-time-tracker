package com.time.time_traking.controller;

import com.time.time_traking.DTO.EmployeeDto;
import com.time.time_traking.model.ChefService;
import com.time.time_traking.model.Employee;
import com.time.time_traking.model.Manager;
import com.time.time_traking.service.ChefServices;
import com.time.time_traking.service.EmployeeService;
import com.time.time_traking.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")

public class RoleDataController {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private ChefServices chefServiceService;

    @GetMapping("/employees/{id}/service-email")
    public ResponseEntity<EmployeeDto> getEmployeeServiceEmail(@PathVariable Long id) {
        Optional<Employee> employeeOptional = employeeService.getEmployeeByI(id);
        return employeeOptional.map(employee -> ResponseEntity.ok(new EmployeeDto(employee.getEmail(), employee.getService())))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/managers/{id}/service-email")
    public ResponseEntity<EmployeeDto> getManagerServiceEmail(@PathVariable Long id) {
        Optional<Manager> managerOptional = managerService.getManagerById(id);
        return managerOptional.map(manager -> ResponseEntity.ok(new EmployeeDto(manager.getEmail(), manager.getService())))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/chef-services/{id}/service-email")
    public ResponseEntity<EmployeeDto> getChefServiceServiceEmail(@PathVariable Long id) {
        Optional<ChefService> chefServiceOptional = chefServiceService.getChefServiceById(id);
        return chefServiceOptional.map(chefService -> ResponseEntity.ok(new EmployeeDto(chefService.getEmail(), chefService.getService())))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
