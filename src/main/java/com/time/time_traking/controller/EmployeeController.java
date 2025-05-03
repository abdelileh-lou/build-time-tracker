package com.time.time_traking.controller;

import com.time.time_traking.model.*;
import com.time.time_traking.service.EmployeeService;
import com.time.time_traking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private UserService userService;

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
    public ResponseEntity<Employee> addEmployee(
            @RequestBody Employee employee) {

        Employee savedEmployee = employeeService.addEmployee(employee);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    @PostMapping("/manager")
    public ResponseEntity<Manager> addManager(
            @RequestBody Manager manager,
            @RequestParam(required = false) List<Double> facialDescriptor) {

        Manager savedManager = employeeService.addManager(manager, facialDescriptor);
        return new ResponseEntity<>(savedManager, HttpStatus.CREATED);
    }

    @PostMapping("/chef-service")
    public ResponseEntity<ChefService> addChefService(
            @RequestBody ChefService chefService,
            @RequestParam(required = false) List<Double> facialDescriptor) {

        ChefService savedChef = employeeService.addChefService(chefService, facialDescriptor);
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



//    @PutMapping("/employee/{id}")
//    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestPart Employee employee , @RequestPart MultipartFile imageFile) {
//
//        Employee updatedEmployee = employeeService.updateEmployee(id, employee , imageFile);
//        if (updatedEmployee != null) {
//            return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//    }


    @PutMapping("/employee/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String password,
            @RequestPart(required = false) MultipartFile imageFile
    ) {
        try {
            Employee updatedEmployee = employeeService.updateEmployee(
                    id, name, email, password, imageFile
            );
            return ResponseEntity.ok(updatedEmployee);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    //Showing employees from your service only
    @GetMapping("/employees/my-service")
    public ResponseEntity<List<Employee>> getEmployeesByMyService(Authentication authentication) {
        try {
            // Get current user's username
            String username = authentication.getName();

            // Find user by username, throw exception if not found
            User user = userService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));


            // Get the service from the user
            String service = user.getServices();

            // Get employees by service and role
            List<Employee> employees = employeeService.getEmployeesByService(service);

            return new ResponseEntity<>(employees, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }




//    @PostMapping("/verify-face")
//    public ResponseEntity<?> verifyFace(@RequestBody Map<String, Object> request) {
//        try {
//            Long employeeId = Long.parseLong(request.get("employeeId").toString());
//            List<Double> currentDescriptor = (List<Double>) request.get("descriptor");
//
//            Employee employee = employeeService.findEmployeeById(employeeId);
//
//            List<Double> storedDescriptor = convertBytesToDescriptor(employee.getFacialData());
//
//            if (currentDescriptor.size() != storedDescriptor.size()) {
//                return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Descriptor size mismatch"));
//            }
//
//            double distance = calculateDistance(currentDescriptor, storedDescriptor);
//
//            return ResponseEntity.ok().body(Collections.singletonMap("match", distance < 0.6));
//        } catch (NumberFormatException e) {
//            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Invalid employee ID"));
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", e.getMessage()));
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body(Collections.singletonMap("error", "Internal server error"));
//        }
//    }

    // Helper Methods
    private List<Double> convertBytesToDescriptor(byte[] bytes) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             DataInputStream dis = new DataInputStream(bis)) {

            int numDoubles = bytes.length / Double.BYTES;
            List<Double> descriptor = new ArrayList<>(numDoubles);

            for (int i = 0; i < numDoubles; i++) {
                descriptor.add(dis.readDouble());
            }
            return descriptor;
        } catch (IOException e) {
            throw new RuntimeException("Error reading facial descriptor", e);
        }
    }

    private double calculateDistance(List<Double> d1, List<Double> d2) {
        if (d1.size() != d2.size()) {
            throw new IllegalArgumentException("Descriptors must be of the same length");
        }

        double sum = 0;
        for (int i = 0; i < d1.size(); i++) {
            sum += Math.pow(d1.get(i) - d2.get(i), 2);
        }
        return Math.sqrt(sum);
    }
}