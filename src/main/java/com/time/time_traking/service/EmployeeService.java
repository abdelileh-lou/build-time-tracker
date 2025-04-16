package com.time.time_traking.service;

import com.time.time_traking.model.*;
import com.time.time_traking.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public Employee addEmployee(Employee employee) {
//        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        Employee savedEmployee = employeeRepository.save(employee);
        userService.registerUser(employee.getUsername(), employee.getPassword(), employee.getRole() , employee.getDepartment());
        return savedEmployee;
    }

    public Manager addManager(Manager manager) {
//        manager.setPassword(passwordEncoder.encode(manager.getPassword()));
        Manager savedManager = employeeRepository.save(manager);
        userService.registerUser(manager.getUsername(), manager.getPassword(), manager.getRole() , manager.getDepartment());
        return savedManager;
    }

    public ChefService addChefService(ChefService chefService) {
//        chefService.setPassword(passwordEncoder.encode(chefService.getPassword()));
        ChefService savedChef = employeeRepository.save(chefService);
        userService.registerUser(chefService.getUsername(), chefService.getPassword(), chefService.getRole() , chefService.getDepartment());
        return savedChef;
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    public List<Employee> getEmployeesByRole(Role role) {
        return employeeRepository.findByRole(role);
    }

// chef delete
    public List<Employee> getEmployeesByDepartment(String department) {
        return employeeRepository.findByDepartment(department);
    }


    public List<Employee> getAllEmployeesExceptChefAndManger() {
        return  employeeRepository.findEmployeesByRole(Role.employee);
    }
}