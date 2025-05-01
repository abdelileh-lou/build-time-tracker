package com.time.time_traking.service;

import com.time.time_traking.model.*;
import com.time.time_traking.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        // Create User first
        User user = userService.registerUser(
                employee.getUsername(),
                employee.getPassword(),
                employee.getRole(),
                employee.getService()
        );

        // Link the User to the Employee
        employee.setUser(user);

        // Save the Employee
        Employee savedEmployee = employeeRepository.save(employee);
        return savedEmployee;
    }

    public Manager addManager(Manager manager) {
        User user = userService.registerUser(
                manager.getUsername(),
                manager.getPassword(),
                manager.getRole(),
                manager.getService()
        );

        manager.setUser(user);
        Manager savedManager = employeeRepository.save(manager);
        return savedManager;
    }

    public ChefService addChefService(ChefService chefService) {
        User user = userService.registerUser(
                chefService.getUsername(),
                chefService.getPassword(),
                chefService.getRole(),
                chefService.getService()
        );

        chefService.setUser(user);
        ChefService savedChef = employeeRepository.save(chefService);
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


    //new
    public Planning getPlanning(Long id) {
//        Employee emp  = employeeRepository.findById(id).orElse(null);
//        if (emp == null) {}
//        return


        Employee emp = employeeRepository.findById(id).orElse(null);
        if (emp == null) {
            return null;
        }else {
            return emp.getPlanning();
        }
//         Planning planning = employeeRepository.findPlanningById(id);
//         return planning;
    }



    // EmployeeService.java
    public Optional<Employee> findEmployeeByUser(User user) {
        return employeeRepository.findByUser(user);
    }
}