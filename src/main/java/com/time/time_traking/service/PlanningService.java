package com.time.time_traking.service;

import com.time.time_traking.model.Employee;
import com.time.time_traking.model.Planning;
import com.time.time_traking.repository.PlanningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanningService {
    @Autowired
    private PlanningRepository planningRepository;

    @Autowired
    private EmployeeService employeeService;

    public Planning createPlanning(Planning planning) {
        // Validate required fields

        if (planning.getEmployee() == null) {
            throw new IllegalArgumentException("Employee must be specified");
        }
        if (planning.getDate() == null) {
            throw new IllegalArgumentException("Date must be specified");
        }
        if (planning.getStartTime() == null || planning.getEndTime() == null) {
            throw new IllegalArgumentException("Start and end times must be specified");
        }

        if (planning.getDepartment() == null || planning.getDepartment().isEmpty()) {
            throw new IllegalArgumentException("Department must be specified");
        }
        // Ensure employee exists
        Employee employee = employeeService.getEmployeeById(planning.getEmployee().getId());
        if (employee == null) {
            throw new IllegalArgumentException("Employee not found");
        }

        planning.setEmployee(employee);
        return planningRepository.save(planning);
    }

    public List<Planning> getDepartmentPlanning(String department) {
        return planningRepository.findByDepartment(department);
    }

    public void deletePlanning(Long id) {
        planningRepository.deleteById(id);
    }
}