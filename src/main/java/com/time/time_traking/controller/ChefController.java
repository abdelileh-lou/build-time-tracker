package com.time.time_traking.controller;

import com.time.time_traking.DTO.AttendanceRecordDTO;
import com.time.time_traking.service.ChefServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chef")
public class ChefController {

    private final ChefServices chefService;

    public ChefController(ChefServices chefService) {
        this.chefService = chefService;
    }

    @GetMapping("/records")
    public ResponseEntity<List<AttendanceRecordDTO>> getAttendanceRecords() {
        List<AttendanceRecordDTO> records = chefService.getAttendanceRecords();
        return ResponseEntity.ok(records);
    }
} 