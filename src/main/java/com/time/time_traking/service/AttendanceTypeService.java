package com.time.time_traking.service;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.time.time_traking.model.AttendanceType;
import com.time.time_traking.repository.AttendanceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class AttendanceTypeService {

    private final AttendanceTypeRepository repository;

    public AttendanceTypeService(AttendanceTypeRepository repository) {
        this.repository = repository;
    }

    public List<AttendanceType> getAllAttendanceTypes() {
        return repository.findAll();
    }

    public void deleteAttendanceType(Long id) {
        repository.deleteById(id);
    }






}
