package com.time.time_traking.controller;


import com.time.time_traking.DTO.AttendanceMethodsDTO;
import com.time.time_traking.DTO.MethodConfig;
import com.time.time_traking.model.AttendanceConfiguration;
import com.time.time_traking.service.AttendanceConfigurationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendance-methods")
public class AttendanceConfigurationController {
    private final AttendanceConfigurationService service;

    public AttendanceConfigurationController(AttendanceConfigurationService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<AttendanceMethodsDTO> getAttendanceMethods() {
        AttendanceConfiguration config = service.getAttendanceConfiguration();
        AttendanceMethodsDTO dto = mapToDTO(config);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<AttendanceMethodsDTO> updateAttendanceMethods(@RequestBody AttendanceMethodsDTO dto) {
        AttendanceConfiguration updatedConfig = service.updateAttendanceConfiguration(dto);
        AttendanceMethodsDTO responseDto = mapToDTO(updatedConfig);
        return ResponseEntity.ok(responseDto);
    }

    private AttendanceMethodsDTO mapToDTO(AttendanceConfiguration config) {
        AttendanceMethodsDTO dto = new AttendanceMethodsDTO();

        MethodConfig qrConfig = new MethodConfig();
        qrConfig.setActive(config.isQrCodeActive());
        qrConfig.setPriority(config.getQrCodePriority());
        dto.setQrCode(qrConfig);

        MethodConfig facialConfig = new MethodConfig();
        facialConfig.setActive(config.isFacialRecognitionActive());
        facialConfig.setPriority(config.getFacialRecognitionPriority());
        dto.setFacialRecognition(facialConfig);

        return dto;
    }
}
