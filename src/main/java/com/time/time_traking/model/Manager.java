package com.time.time_traking.model;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
@DiscriminatorValue("MANAGER")
public class Manager extends Employee {
    private boolean canViewAttendance = true;
    private boolean canReceiveNotification = true;

    public Manager() {
        this.setRole(Role.manager);
    }
}
