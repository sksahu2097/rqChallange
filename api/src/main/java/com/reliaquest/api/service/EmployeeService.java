package com.reliaquest.api.service;

import com.reliaquest.api.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "http://localhost:8112/api/v1/employee";

    public List<Employee> getAllEmployees() {
        ResponseEntity<ApiResponse<List<Employee>>> response =
                restTemplate.exchange(BASE_URL, HttpMethod.GET, null,
                        new org.springframework.core.ParameterizedTypeReference<ApiResponse<List<Employee>>>(){});
        return response.getBody().getData();
    }

    public List<Employee> searchByName(String nameFragment) {
        return getAllEmployees().stream()
                .filter(emp -> emp.getEmployee_name().toLowerCase().contains(nameFragment.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Employee getById(String id) {
        ResponseEntity<ApiResponse<Employee>> response =
                restTemplate.exchange(BASE_URL + "/" + id, HttpMethod.GET, null,
                        new org.springframework.core.ParameterizedTypeReference<ApiResponse<Employee>>(){});
        return response.getBody().getData();
    }

    public Integer getHighestSalary() {
        return getAllEmployees().stream()
                .map(Employee::getEmployee_salary)
                .max(Integer::compareTo)
                .orElse(0);
    }

    public List<String> getTop10HighestEarningEmployeeNames() {
        return getAllEmployees().stream()
                .sorted((a, b) -> b.getEmployee_salary().compareTo(a.getEmployee_salary()))
                .limit(10)
                .map(Employee::getEmployee_name)
                .collect(Collectors.toList());
    }

    public Employee createEmployee(EmployeeInput input) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<EmployeeInput> request = new HttpEntity<>(input, headers);

        ResponseEntity<ApiResponse<Employee>> response =
                restTemplate.exchange(BASE_URL, HttpMethod.POST, request,
                        new org.springframework.core.ParameterizedTypeReference<ApiResponse<Employee>>(){});
        return response.getBody().getData();
    }

    public String deleteEmployeeById(String id) {
        ResponseEntity<ApiResponse<Boolean>> response =
                restTemplate.exchange(BASE_URL + "/" + id, HttpMethod.DELETE, null,
                        new org.springframework.core.ParameterizedTypeReference<ApiResponse<Boolean>>(){});

        if (Boolean.TRUE.equals(response.getBody().getData())) {
            return "Employee with id " + id + " deleted successfully";
        } else {
            throw new RuntimeException("Failed to delete employee with id " + id);
        }
    }
}
