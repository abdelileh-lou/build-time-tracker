package com.time.time_traking.controller;


import com.time.time_traking.model.Planning;
import com.time.time_traking.service.PlanningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planning")
public class PlanningController {
    @Autowired
    private PlanningService planningService;

    @PostMapping
    public ResponseEntity<Planning> createPlanning(@RequestBody Planning planning) {
        Planning createdPlanning = planningService.createPlanning(planning);
        return ResponseEntity.ok(createdPlanning);
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<Planning>> getPlanningByDepartment(@PathVariable String department) {
        List<Planning> plannings = planningService.getDepartmentPlanning(department);
        return ResponseEntity.ok(plannings);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlanning(@PathVariable Long id) {
        planningService.deletePlanning(id);
        return ResponseEntity.noContent().build();
    }
}
