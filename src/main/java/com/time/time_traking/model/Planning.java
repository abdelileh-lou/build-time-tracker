package com.time.time_traking.model;

import com.time.time_traking.model.Employee;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Planning {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    private LocalDate date;
    private String startTime;
    private String endTime;
    private Double hours;
    private String description;
    @Column(nullable = false)
    private String department;

    // Make sure you have proper getters and setters
    // Lombok @Data should handle this, but you might need custom ones
}