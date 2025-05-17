package com.time.time_traking.controller;

import com.time.time_traking.DTO.AttendanceRecordDTO;
import com.time.time_traking.model.ChefService;
import com.time.time_traking.service.ChefServices;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/{id}/details")
    public ResponseEntity<ChefService> getChefServiceDetails(@PathVariable Long id) {
        ChefService chefServ = chefService.getChefServiceDetailsById(id);
        return ResponseEntity.ok(chefServ);
    }

} 