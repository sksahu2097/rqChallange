package com.reliaquest.api.service;

import com.reliaquest.api.model.ApiResponse;
import com.reliaquest.api.model.Employee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void testGetHighestSalary() {
        Employee e1 = new Employee();
        e1.setEmployee_name("A");
        e1.setEmployee_salary(100);

        Employee e2 = new Employee();
        e2.setEmployee_name("B");
        e2.setEmployee_salary(200);

        ApiResponse<List<Employee>> mockResponse = new ApiResponse<>();
        mockResponse.setData(Arrays.asList(e1, e2));
        mockResponse.setStatus("ok");

        Mockito.when(restTemplate.exchange(
                Mockito.anyString(),
                Mockito.eq(HttpMethod.GET),
                Mockito.isNull(),
                Mockito.<ParameterizedTypeReference<ApiResponse<List<Employee>>>>any()
        )).thenReturn(ResponseEntity.ok(mockResponse));

        Integer result = employeeService.getHighestSalary();
        assertEquals(200, result);
    }
}

