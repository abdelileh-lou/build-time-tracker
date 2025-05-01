package com.time.time_traking.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.time.time_traking.DTO.PlanningDTO;
import com.time.time_traking.model.ChefService;
import com.time.time_traking.model.Employee;
import com.time.time_traking.model.Planning;
import com.time.time_traking.service.EmployeeService;
import com.time.time_traking.service.PlanningService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/planning")
public class PlanningController {
    private final PlanningService planningService;

    //deleted
    private final EmployeeService employeeService;
    private final ObjectMapper objectMapper;

    public PlanningController(PlanningService planningService , EmployeeService employeeService , ObjectMapper objectMapper) {
        this.planningService = planningService;
        this.employeeService = employeeService;
        this.objectMapper = objectMapper;
    }

//    @PostMapping
//    public ResponseEntity<?> createPlanning(@RequestBody Map<String, String> request) {
//        try {
//            String planJson = request.get;
//            return ResponseEntity.ok(planningService.savePlanning(planJson));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid planning data");
//        }
//    }



//    @PostMapping
//    public ResponseEntity<Planning> addPlanning(@RequestBody Planning planning) {
//        Planning savedPlanning = planningService.savePlanning(planning);
//        return new ResponseEntity<>(savedPlanning, HttpStatus.CREATED);
//    }

//    @PostMapping
//    public ResponseEntity<Planning> addPlanning(@RequestBody Map<String, String> request) {
//        Planning newPlanning = new Planning();
//        newPlanning.setPlanJson(request.get("planJson")); // Match frontend key
//        Planning savedPlanning = planningService.savePlanning(newPlanning);
//        return new ResponseEntity<>(savedPlanning, HttpStatus.CREATED);
//    }



    @PostMapping
    public ResponseEntity<Planning> addPlanning(@RequestBody Map<String, String> request) {
        Planning newPlanning = new Planning();
        newPlanning.setPlanJson(request.get("planning"));  // Match frontend's key name
        Planning savedPlanning = planningService.savePlanning(newPlanning);
        return new ResponseEntity<>(savedPlanning, HttpStatus.CREATED);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<?> deletePlanning(@PathVariable String name) {
        try {
            planningService.deletePlanningByName(name);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    //old
//    @PostMapping("/assign-planning")
//    public ResponseEntity<?> assignPlanning(@RequestBody Map<String, String> request) {
//        try {
//            String planningName = request.get("planningName");
//            String planJson = planningService.getPlanningByName(planningName);
//            // Add your business logic here to assign planning to employees
//            return ResponseEntity.ok().body("Planning assigned successfully");
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }
//    }



    //del
    @PostMapping("/assign-planning")
    public ResponseEntity<?> assignPlanning(@RequestBody Map<String, Object> request) {
        try {
            String planningName = (String) request.get("planningName");
            List<Long> employeeIds = (List<Long>) request.get("employeeIds");
            planningService.assignPlanningToEmployees(planningName, employeeIds);
            return ResponseEntity.ok().body("Planning assigned successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }




}