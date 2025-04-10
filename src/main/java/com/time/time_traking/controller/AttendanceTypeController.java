package com.time.time_traking.controller;

import com.time.time_traking.model.AttendanceType;
import com.time.time_traking.service.AttendanceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AttendanceTypeController {

    @Autowired
    private AttendanceTypeService attendanceTypeService;

    @GetMapping("/attendance-types")
    public ResponseEntity<List<AttendanceType>> getAttendanceTypes() {
        List<AttendanceType> attendanceTypes = attendanceTypeService.getAllAttendanceTypes();
        return ResponseEntity.ok(attendanceTypes);
    }

    @GetMapping("/attendance-type/{id}")
    public ResponseEntity<AttendanceType> getAttendanceTypeById(@PathVariable Long id) {
        AttendanceType attendanceType = attendanceTypeService.getAttendanceTypeById(id);
        if (attendanceType != null) {
            return new ResponseEntity<>(attendanceType, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/attendance-type/{id}/qrcode")
    public ResponseEntity<byte[]> getAttendanceTypeQRCode(@PathVariable Long id) {
        AttendanceType attendanceType = attendanceTypeService.getAttendanceTypeById(id);
        if (attendanceType == null || attendanceType.getQrCode() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        byte[] qrCodeBytes = Base64.getDecoder().decode(attendanceType.getQrCode());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(qrCodeBytes);
    }

    @PostMapping("/attendance-type")
    public ResponseEntity<AttendanceType> addAttendanceType(@RequestBody AttendanceType attendanceType) {
        AttendanceType savedAttendanceType = attendanceTypeService.addAttendanceType(attendanceType);
        return new ResponseEntity<>(savedAttendanceType, HttpStatus.CREATED);
    }

    @DeleteMapping("/attendance-type/{id}")
    public ResponseEntity<AttendanceType> deleteAttendanceType(@PathVariable Long id) {
        AttendanceType attendanceType = attendanceTypeService.getAttendanceTypeById(id);
        if (attendanceType != null) {
            attendanceTypeService.deleteAttendanceType(id);
            return new ResponseEntity<>(attendanceType, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }





}
