package com.time.time_traking.model;

public enum Role {


    admin("Administrator"),
    manager("Manager"),
    chef("Chef"),
    employee("Employee");

    private final String role;

    Role(String role) {
        this.role = role;
    }


}
