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
public class AttendanceRecordDTO {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private LocalDateTime timestamp;
    private String status;
    private boolean notifiedManager;
    private boolean reportedChef;
}
