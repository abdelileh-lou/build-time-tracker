package com.time.time_traking.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AttendanceConfiguration {

    @Id
    private Long id = 1L; // Manually assign ID
    private boolean qrCodeActive;
    private int qrCodePriority;
    private boolean facialRecognitionActive;
    private int facialRecognitionPriority;

    // new attendance methods
    private boolean pinCodeActive;
    private int pinCodePriority;
}
