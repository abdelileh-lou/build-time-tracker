package com.time.time_traking.service;

import com.time.time_traking.DTO.AttendanceMethodsDTO;
import com.time.time_traking.model.AttendanceConfiguration;
import com.time.time_traking.repository.AttendanceConfigurationRepository;
import org.springframework.stereotype.Service;

@Service
public class AttendanceConfigurationService {
    private final AttendanceConfigurationRepository repository;

    public AttendanceConfigurationService(AttendanceConfigurationRepository repository) {
        this.repository = repository;
    }

    public AttendanceConfiguration getAttendanceConfiguration() {
        return repository.findById(1L).orElseGet(() -> {
            AttendanceConfiguration config = new AttendanceConfiguration();
            config.setId(1L);
            config.setQrCodeActive(true);
            config.setQrCodePriority(1);
            config.setFacialRecognitionActive(true);
            config.setFacialRecognitionPriority(2);
            return repository.save(config);
        });
    }
    public AttendanceConfiguration updateAttendanceConfiguration(AttendanceMethodsDTO dto) {
        AttendanceConfiguration config = getAttendanceConfiguration();
        config.setQrCodeActive(dto.getQrCode().isActive());
        config.setQrCodePriority(dto.getQrCode().getPriority());
        config.setFacialRecognitionActive(dto.getFacialRecognition().isActive());
        config.setFacialRecognitionPriority(dto.getFacialRecognition().getPriority());

        // new attendance methods
        config.setPinCodeActive(dto.getPinCode().isActive());
        config.setPinCodePriority(dto.getPinCode().getPriority());
        return repository.save(config);
    }
}
