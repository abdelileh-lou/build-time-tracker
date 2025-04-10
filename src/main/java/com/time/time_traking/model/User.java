package com.time.time_traking.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;



    @Column(unique = true , nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

//    @Column(nullable = false) // Stocke directement en String
//    private String role;
    @Enumerated(EnumType.STRING)
    private Role role;
}
