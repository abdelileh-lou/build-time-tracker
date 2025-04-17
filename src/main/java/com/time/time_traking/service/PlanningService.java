package com.time.time_traking.service;

import com.time.time_traking.model.Planning;
import com.time.time_traking.repository.PlanningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanningService {
    @Autowired
    private PlanningRepository Planningrepository;
    @Autowired
    private PlanningRepository planningRepository;

    public Planning createPlanning(Planning planning) {
        return Planningrepository.save(planning);
    }

    public List<Planning> getDepartmentPlanning(String department) {
        return planningRepository.findByDepartment(department);
    }

    public void deletePlanning(Long id) {
        planningRepository.deleteById(id);
    }
}
