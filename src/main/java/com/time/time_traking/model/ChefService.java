package com.time.time_traking.model;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
@DiscriminatorValue("CHEF_SERVICE")
public class ChefService extends Employee {
    private boolean canCreatePlanning = true;
    private boolean canGenerateReport = true;

    public ChefService() {
        this.setRole(Role.chef);
    }
}
