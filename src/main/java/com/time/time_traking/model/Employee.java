package com.time.time_traking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employee")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "employee_type", discriminatorType = DiscriminatorType.STRING)
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Email
    private String email;

    @Column(unique = true, nullable = false)
    private String username;



    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "employee_type", insertable = false, updatable = false)
    private String employeeType;


    @ManyToOne
    @JoinColumn(name = "planning_id")
    private Planning planning;


    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


    @Transient
    private String password;

    @Transient
    private String service;
}