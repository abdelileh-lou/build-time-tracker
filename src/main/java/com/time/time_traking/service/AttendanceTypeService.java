package com.time.time_traking.service;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.time.time_traking.model.AttendanceType;
import com.time.time_traking.repository.AttendanceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class AttendanceTypeService {

    @Autowired
    private AttendanceTypeRepository attendanceTypeRepository;

    public List<AttendanceType> getAllAttendanceTypes() {
        return attendanceTypeRepository.findAll();
    }

    public  AttendanceType getAttendanceTypeById(Long attendanceTypeId) {
        return attendanceTypeRepository.findById(attendanceTypeId).orElse(null);
    }



//    public AttendanceType addAttendanceType(AttendanceType attendanceType) {
//        String qrCode =  generateQRCode(attendanceType.getName());
//        attendanceType.setQrCode(qrCode);
//        return attendanceTypeRepository.save(attendanceType);
//    }


    public AttendanceType addAttendanceType(AttendanceType attendanceType) {
        // First save to get an ID
        AttendanceType savedType = attendanceTypeRepository.save(attendanceType);

        // Now generate QR code with the actual ID
        String qrContent = "ATTENDANCE_TYPE:" + savedType.getId();
        String qrCode = generateQRCode(qrContent);

        // Update with QR code and save again
        savedType.setQrCode(qrCode);
        return attendanceTypeRepository.save(savedType);
    }

    public void deleteAttendanceType(Long attendanceTypeId) {
        attendanceTypeRepository.deleteById(attendanceTypeId);
    }


    private String generateQRCode(String text) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray();
            return Base64.getEncoder().encodeToString(pngData);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
