package com.time.time_traking.controller;


import com.time.time_traking.model.Employee;
import com.time.time_traking.model.Role;
import com.time.time_traking.model.User;
import com.time.time_traking.security.JwtUtil;
import com.time.time_traking.service.EmployeeService;
import com.time.time_traking.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")

public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeService employeeService;



    public AuthController(UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder , EmployeeService employeeService) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.employeeService = employeeService;

    }

    @PostMapping("/register")
    public User register(@RequestBody User requestBody) {
        return userService.registerUser(
                requestBody.getUsername(),
                requestBody.getPassword(),
                requestBody.getRole(),
                requestBody.getServices()  // Add department
        );
    }







    // Login Controller Method
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User requestBody) {
        Optional<User> user = userService.findByUsername(requestBody.getUsername());

        if (user.isPresent() && passwordEncoder.matches(requestBody.getPassword(), user.get().getPassword())) {
            User loggedInUser = user.get();
            String token = jwtUtil.generateToken(loggedInUser.getUsername());
            Role role = loggedInUser.getRole();

            // For admin, return user's ID as employeeId
            if (role == Role.admin) {
                return Map.of(
                        "token", token,
                        "role", role.name(),
                        "employeeId", loggedInUser.getId().toString() // Include user's ID
                );
            }
            // For non-admin roles, fetch the associated Employee
            else {
                Employee employee = employeeService.findEmployeeByUser(loggedInUser)
                        .orElseThrow(() -> new RuntimeException("Employee not found for user"));
                return Map.of(
                        "token", token,
                        "role", role.name(),
                        "employeeId", employee.getId().toString()
                );
            }
        }
        throw new RuntimeException("Invalid credentials");
    }



    // AuthController.java - Add proper security configuration
    @GetMapping("/current-user")
    public ResponseEntity<Map<String, Object>> getCurrentUser(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = principal.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> userData = new HashMap<>();
        userData.put("username", user.getUsername());
        userData.put("role", user.getRole().name());

        // Handle null services more explicitly
        userData.put("services", user.getServices() != null ? user.getServices() : "");

        // For non-admin users, add employeeId if needed
        if (user.getRole() != Role.admin) {
            Employee employee = employeeService.findEmployeeByUser(user)
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
            userData.put("employeeId", employee.getId());
        }

        return ResponseEntity.ok(userData);
    }

}
