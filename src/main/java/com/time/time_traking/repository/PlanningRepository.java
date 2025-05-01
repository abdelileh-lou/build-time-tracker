package com.time.time_traking.repository;


import com.time.time_traking.model.Planning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanningRepository  extends JpaRepository<Planning, Long> {


}
