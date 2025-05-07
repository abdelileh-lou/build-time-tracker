package com.time.time_traking.service;

import com.time.time_traking.model.*;
import com.time.time_traking.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        // Convert facial data if exists
//        if(facialDescriptor != null) {
//            byte[] facialBytes = convertFacialDescriptorToBytes(facialDescriptor);
//            employee.setFacialData(facialBytes);
//        }

        // Link the User to the Employee
        employee.setUser(user);

        return employeeRepository.save(employee);
    }

    private byte[] convertFacialDescriptorToBytes(List<Double> descriptor) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            for (Double d : descriptor) {
                dos.writeDouble(d);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error converting facial descriptor", e);
        }
    }

//    public Manager addManager(Manager manager, List<Double> facialDescriptor) {
//        User user = userService.registerUser(
//                manager.getUsername(),
//                manager.getPassword(),
//                Role.manager,
//                manager.getService()
//        );
//
////        if(facialDescriptor != null) {
////            byte[] facialBytes = convertFacialDescriptorToBytes(facialDescriptor);
////            manager.setFacialData(facialBytes);
////        }
//
//        manager.setUser(user);
//        return employeeRepository.save(manager);
//    }

public Manager addManager(Manager manager, List<Double> facialDescriptor) {
    User user = userService.registerUser(
            manager.getUsername(),
            manager.getPassword(),
            Role.manager,
            manager.getService()
    );

    if (facialDescriptor != null) {
        // Convert list of doubles to comma-separated string
        String facialData = facialDescriptor.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        manager.setFacialData(facialData);
    }

    manager.setUser(user);
    return employeeRepository.save(manager);
}

    public ChefService addChefService(ChefService chefService, List<Double> facialDescriptor) {
        User user = userService.registerUser(
                chefService.getUsername(),
                chefService.getPassword(),
                Role.chef,
                chefService.getService()
        );
//
//        if(facialDescriptor != null) {
//            byte[] facialBytes = convertFacialDescriptorToBytes(facialDescriptor);
//            chefService.setFacialData(facialBytes);
//        }

        chefService.setUser(user);
        return employeeRepository.save(chefService);
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

    public Employee updateEmployee(
            Long id,
            String name,
            String email,
            String password,
            MultipartFile imageFile
    ) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Update basic fields
        if (name != null) existingEmployee.setName(name);
        if (email != null) existingEmployee.setEmail(email);

        // Update password in associated User entity
        if (password != null && !password.isEmpty()) {
            User user = existingEmployee.getUser();
            user.setPassword(passwordEncoder.encode(password));
            userService.updateUser(user);  // Make sure you have this service method
        }

        // Handle image update
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                existingEmployee.setImageName(imageFile.getOriginalFilename());
                existingEmployee.setImageType(imageFile.getContentType());
                existingEmployee.setImageData(imageFile.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to process image file", e);
            }
        }

        return employeeRepository.save(existingEmployee);
    }


    //Showing employees from your service only
    public List<Employee> getEmployeesByService(String service) {
        return employeeRepository.findEmployeesByServiceAndRoleEmployee(service);
    }



    //deleted
    public Employee findEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    public String getFacialDataByEmployeeId(Long id) {
        Employee employee = getEmployeeById(id);
        if (employee == null) {
            throw new RuntimeException("Employee not found");
        }
        return employee.getFacialData();
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }


    public Employee findById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }



}