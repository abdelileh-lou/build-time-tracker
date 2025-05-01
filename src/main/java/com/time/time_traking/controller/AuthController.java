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

//    @PostMapping("/login")
//    public Map<String, String> login(@RequestBody User requestBody) {
//        Optional<User> user = userService.findByUsername(requestBody.getUsername());
//
//
//        if (user.isPresent() &&  passwordEncoder.matches(requestBody.getPassword(), user.get().getPassword())) {
//            String token = jwtUtil.generateToken(user.get().getUsername());
//            return Map.of("token", token, "role", user.get().getRole().name());
//        }
//        throw new RuntimeException("Invalid credentials");
//
//    }

    
    // this will be used  to get employeeId

//    @PostMapping("/login")
//    public Map<String, String> login(@RequestBody User requestBody) {
//        Optional<User> user = userService.findByUsername(requestBody.getUsername());
//
//        if (user.isPresent() && passwordEncoder.matches(requestBody.getPassword(), user.get().getPassword())) {
//            String token = jwtUtil.generateToken(user.get().getUsername());
//            return Map.of(
//                    "token", token,
//                    "role", user.get().getRole().name(),
//                    "employeeId", user.get().getId().toString() // Ensure this is the correct ID
//            );
//        }
//        throw new RuntimeException("Invalid credentials");
//    }





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



}
