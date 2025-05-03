package com.time.time_traking.service;


import com.time.time_traking.DTO.PlanningDTO;
import com.time.time_traking.model.Employee;
import com.time.time_traking.model.Planning;
import com.time.time_traking.repository.EmployeeRepository;
import com.time.time_traking.repository.PlanningRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PlanningService {
    private final PlanningRepository planningRepository;
    private final ObjectMapper objectMapper;


    private EmployeeRepository employeeRepository;


    public PlanningService(PlanningRepository planningRepository, ObjectMapper objectMapper , EmployeeRepository employeeRepository) {
        this.planningRepository = planningRepository;
        this.objectMapper = objectMapper;
        this.employeeRepository = employeeRepository;
    }



    public Planning savePlanning(Planning planning) {
        return planningRepository.save(planning);
    }



    public void deletePlanningByName(String name) throws IOException {
        List<Planning> allPlannings = planningRepository.findAll();
        for (Planning planning : allPlannings) {
            JsonNode jsonNode = objectMapper.readTree(planning.getPlanJson());
            if (jsonNode.has("name") && jsonNode.get("name").asText().equals(name)) {
                planningRepository.delete(planning);
                return;
            }
        }
        throw new IllegalArgumentException("Planning not found with name: " + name);
    }

    public String getPlanningByName(String name) throws IOException {
        List<Planning> allPlannings = planningRepository.findAll();
        for (Planning planning : allPlannings) {
            JsonNode jsonNode = objectMapper.readTree(planning.getPlanJson());
            if (jsonNode.has("name") && jsonNode.get("name").asText().equals(name)) {
                return planning.getPlanJson();
            }
        }
        throw new IllegalArgumentException("Planning not found with name: " + name);
    }




    //del
    public void assignPlanningToEmployees(String planningName, List<Long> employeeIds) throws IOException {
        Planning planning = findPlanningEntityByName(planningName);
        List<Employee> employees = employeeRepository.findAllById(employeeIds);

        for (Employee employee : employees) {
            employee.setPlanning(planning);
        }
        employeeRepository.saveAll(employees);
    }

    //del
    private Planning findPlanningEntityByName(String name) throws IOException {
        List<Planning> allPlannings = planningRepository.findAll();
        for (Planning planning : allPlannings) {
            JsonNode jsonNode = objectMapper.readTree(planning.getPlanJson());
            if (jsonNode.has("name") && jsonNode.get("name").asText().equals(name)) {
                return planning;
            }
        }
        throw new IllegalArgumentException("Planning not found with name: " + name);
    }


    public Optional<Planning> findPlanningById(Long planningId) {
        return planningRepository.findById(planningId);
    }


    public List<Planning> getAllPlannings() {
        return planningRepository.findAll();
    }
}