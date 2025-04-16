package com.time.time_traking.model;

import jakarta.persistence.*;
import lombok.Data;

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

    @ManyToOne
    @JoinColumn(name = "attendance_type_id", nullable = false)
    private AttendanceType attendanceType;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    private String status;

    @Column(name = "notified_manager")
    private boolean notifiedManager = false;

    @Column(name = "reported_chef")
    private boolean reportedChef = false;
}