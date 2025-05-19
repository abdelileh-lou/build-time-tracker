package com.time.time_traking.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.*;
import java.util.stream.Collectors;

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






    @GetMapping("/employees/my-service")
    public ResponseEntity<List<Employee>> getEmployeesByMyService(Authentication authentication) {
        String username = authentication.getName();

        User user = userService.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        String service = user.getServices();
        List<Employee> employees = employeeService.getEmployeesByServ(service);

        return new ResponseEntity<>(employees, HttpStatus.OK);
    }




    @PostMapping("/enroll-face")
    public ResponseEntity<?> enrollFace(
            @RequestBody Map<String, Object> request,
            Authentication authentication
    ) {
        try {
            // Add validation for employee existence
            Long employeeId = Long.parseLong(request.get("employeeId").toString());
            Employee employee = employeeService.findEmployeeById(employeeId);
            if (employee == null) {
                return ResponseEntity.badRequest().body(
                        Collections.singletonMap("error", "Employee not found")
                );
            }

            // Validate facial data structure
            if (!validateFacialData(request)) {
                return ResponseEntity.badRequest().body(
                        Collections.singletonMap("error", "Invalid facial data structure")
                );
            }

            // Convert and save
            ObjectMapper mapper = new ObjectMapper();
            String facialDataJson = mapper.writeValueAsString(request);
            employee.setFacialData(facialDataJson);
            employeeService.saveEmployee(employee);

            return ResponseEntity.ok().body(Map.of(
                    "message", "Facial data enrolled successfully",
                    "employeeId", employeeId
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Collections.singletonMap("error", e.getMessage())
            );
        }
    }

    private boolean validateFacialData(Map<String, Object> request) {
        return request.containsKey("descriptor") &&
                request.containsKey("landmarks") &&
                request.containsKey("box") &&
                request.get("descriptor") instanceof List &&
                ((List<?>) request.get("descriptor")).stream().allMatch(n -> n instanceof Number);
    }


    @PostMapping("/verify-face")
    public ResponseEntity<?> verifyFace(
            @RequestBody Map<String, Object> request,
            Authentication authentication
    ) {
        try {
            // Validate request structure
            if (!request.containsKey("employeeId") || !request.containsKey("descriptor")) {
                return ResponseEntity.badRequest().body(
                        Collections.singletonMap("error", "Missing required fields")
                );
            }

            Long employeeId = Long.parseLong(request.get("employeeId").toString());
            List<Double> currentDescriptor = ((List<Number>) request.get("descriptor"))
                    .stream().map(Number::doubleValue).toList();

            Employee employee = employeeService.findEmployeeById(employeeId);
            if (employee.getFacialData() == null) {
                return ResponseEntity.badRequest().body(
                        Collections.singletonMap("error", "No facial data registered")
                );
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode storedData = mapper.readTree(employee.getFacialData());
            List<Double> storedDescriptor = new ArrayList<>();
            for (JsonNode node : storedData.get("descriptor")) {
                storedDescriptor.add(node.asDouble());
            }

            if (currentDescriptor.size() != storedDescriptor.size()) {
                return ResponseEntity.badRequest().body(
                        Collections.singletonMap("error", "Descriptor dimension mismatch")
                );
            }

            double distance = calculateDistance(currentDescriptor, storedDescriptor);
            return ResponseEntity.ok().body(Map.of(
                    "match", distance < 0.6,
                    "confidence", 1 - distance,
                    "employee", Map.of(
                            "id", employee.getId(),
                            "name", employee.getName(),
                            "service", employee.getService()
                    )
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    Collections.singletonMap("error", e.getMessage())
            );
        }
    }



    private double calculateDistance(List<Double> vec1, List<Double> vec2) {
        // Implement your distance calculation logic
        double sum = 0;
        for (int i = 0; i < vec1.size(); i++) {
            sum += Math.pow(vec1.get(i) - vec2.get(i), 2);
        }
        return Math.sqrt(sum);
    }

    private List<Double> parseFacialData(String facialData) {
        return Arrays.stream(facialData.split(","))
                .map(Double::parseDouble)
                .collect(Collectors.toList());
    }

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






//    @GetMapping("/employee/{id}/facial-data")
//    public ResponseEntity<?> getFacialDataEmployee(@PathVariable Long id) {
//        try {
//            Employee employee = employeeService.getEmployeeById(id);
//            if (employee == null) {
//                return ResponseEntity.notFound().build();
//            }
//            String facialDataStr = employee.getFacialData();
//            if (facialDataStr == null || facialDataStr.trim().isEmpty()) {
//                return ResponseEntity.notFound().build();
//            }
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            try {
//                // Parse JSON to extract the "description" array
//                JsonNode rootNode = objectMapper.readTree(facialDataStr);
//                JsonNode descriptionNode = rootNode.get("description");
//
//                if (descriptionNode == null || !descriptionNode.isArray()) {
//                    return ResponseEntity.badRequest().body("Invalid facial data format");
//                }
//
//                List<Double> facialData = new ArrayList<>();
//                for (JsonNode node : descriptionNode) {
//                    if (node.isNumber()) {
//                        facialData.add(node.asDouble());
//                    } else {
//                        return ResponseEntity.badRequest().body("Invalid facial data format");
//                    }
//                }
//
//                if (facialData.isEmpty()) {
//                    return ResponseEntity.notFound().build();
//                }
//
//                return ResponseEntity.ok(facialData);
//            } catch (JsonProcessingException e) {
//                return ResponseEntity.badRequest().body("Invalid facial data format");
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }

    @GetMapping("/employee/{id}/facial-data")
    public ResponseEntity<?> getFacialDataEmployee(@PathVariable Long id) {

        Employee employee = employeeService.getEmployeeById(id);
        if (employee == null) {
            return ResponseEntity.notFound().build();
        }
        String facialDataStr = employee.getFacialData();

        return ResponseEntity.ok(facialDataStr);



    }

    // new to delete
    @GetMapping("/employees-only")
    public ResponseEntity<List<Employee>> getEmployeesOnly() {
        List<Employee> employees = employeeService.getEmployeesOnly();
        return ResponseEntity.ok(employees);
    }

 // codepin
    @GetMapping("/employee/{id}/code-pin")
    public ResponseEntity<String> getEmployeePinCode(@PathVariable Long id) {
        String pin = employeeService.getEmployeePinCode(id);
        return pin != null
                ? ResponseEntity.ok(pin)
                : ResponseEntity.notFound().build();
    }



}