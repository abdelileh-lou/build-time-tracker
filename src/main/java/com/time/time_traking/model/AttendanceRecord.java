package com.time.time_traking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "attendance_record")

public class AttendanceRecord {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

//    @ManyToOne
//    @JoinColumn(name = "attendance_type_id", nullable = false)
//    private AttendanceType attendanceType;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    private String status;

    @Column(name = "notified_manager")
    private boolean notifiedManager = false;

    @Column(name = "reported_chef")
    private boolean reportedChef = false;

    @Column(name = "method")  // Add this field
    private String method;

//    public void setAttendanceType(AttendanceType attendanceType) {
//        this.attendanceType = attendanceType;
//    }

}