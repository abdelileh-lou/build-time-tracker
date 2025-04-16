package com.time.time_traking.controller;


import com.time.time_traking.model.Role;
import com.time.time_traking.model.User;
import com.time.time_traking.security.JwtUtil;
import com.time.time_traking.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")

public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public User register(@RequestBody User requestBody) {
        return userService.registerUser(
                requestBody.getUsername(),
                requestBody.getPassword(),
                requestBody.getRole(),
                requestBody.getDepartment()  // Add department
        );
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User requestBody) {
        Optional<User> user = userService.findByUsername(requestBody.getUsername());


        if (user.isPresent() &&  passwordEncoder.matches(requestBody.getPassword(), user.get().getPassword())) {
            String token = jwtUtil.generateToken(user.get().getUsername());
            return Map.of("token", token, "role", user.get().getRole().name());
        }
        throw new RuntimeException("Invalid credentials");

    }
}
