package com.reliaquest.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EmployeeController implements IEmployeeController{
    @Override
    public ResponseEntity<List> getAllEmployees() {
        return null;
    }

    @Override
    public ResponseEntity<List> getEmployeesByNameSearch(String searchString) {
        return null;
    }

    @Override
    public ResponseEntity getEmployeeById(String id) {
        return null;
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        return null;
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        return null;
    }

    @Override
    public ResponseEntity createEmployee(Object employeeInput) {
        return null;
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        return null;
    }
}
