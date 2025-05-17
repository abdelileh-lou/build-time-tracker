package com.time.time_traking.repository;

import com.time.time_traking.model.ChefService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.beans.JavaBean;

public interface ChefServiceRepository extends JpaRepository<ChefService, Long> {
    @Query("SELECT c FROM ChefService c WHERE c.id = :id")
    ChefService findChefServiceById(@Param("id") Long id);
}
