package com.time.time_traking.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.time.time_traking.model.Planning;
import com.time.time_traking.service.EmployeeService;
import com.time.time_traking.service.PlanningService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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




    @GetMapping("/plannings")
    public ResponseEntity<List<Planning>> getAllPlannings() {
        List<Planning> plannings = planningService.getAllPlannings();
        return ResponseEntity.ok(plannings);
    }

    @GetMapping("/{planningId}")
    public ResponseEntity<Planning> getPlanningById(@PathVariable Long planningId) {
        Optional<Planning> planning = planningService.findPlanningById(planningId);
        return planning.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


}