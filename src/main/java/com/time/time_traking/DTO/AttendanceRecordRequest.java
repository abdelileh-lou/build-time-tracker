package com.time.time_traking.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AttendanceRecordRequest {
    private Long employeeId;
    private String status;
    private LocalDateTime timestamp;


}
