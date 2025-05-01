package com.time.time_traking.controller;


import com.time.time_traking.model.AttendanceType;
import com.time.time_traking.service.AttendanceTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance-types")
public class AttendanceTypeController {
    private final AttendanceTypeService service;

    public AttendanceTypeController(AttendanceTypeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AttendanceType>> getAllAttendanceTypes() {
        return ResponseEntity.ok(service.getAllAttendanceTypes());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttendanceType(@PathVariable Long id) {
        service.deleteAttendanceType(id);
        return ResponseEntity.noContent().build();
    }
}
