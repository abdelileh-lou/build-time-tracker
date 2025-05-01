package com.time.time_traking.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AttendanceMethodsDTO {
    private MethodConfig qrCode;
    private MethodConfig facialRecognition;
}
