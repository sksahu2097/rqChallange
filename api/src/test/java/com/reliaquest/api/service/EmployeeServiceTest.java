package com.reliaquest.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.reliaquest.api.model.ApiResponse;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeInput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

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
                        Mockito.<ParameterizedTypeReference<ApiResponse<List<Employee>>>>any()))
                .thenReturn(ResponseEntity.ok(mockResponse));

        Integer result = employeeService.getHighestSalary();
        assertEquals(200, result);
    }

    private Employee createEmployee(String id, String name, int salary) {
        Employee emp = new Employee();
        emp.setId(id);
        emp.setEmployee_name(name);
        emp.setEmployee_salary(salary);
        return emp;
    }

    @Test
    void testGetAllEmployees_success() {
        List<Employee> employees = Arrays.asList(createEmployee("1", "Alice", 5000), createEmployee("2", "Bob", 6000));
        ApiResponse<List<Employee>> apiResponse = new ApiResponse<>();
        apiResponse.setData(employees);

        ResponseEntity<ApiResponse<List<Employee>>> response = new ResponseEntity<>(apiResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                        anyString(),
                        eq(HttpMethod.GET),
                        isNull(),
                        ArgumentMatchers.<ParameterizedTypeReference<ApiResponse<List<Employee>>>>any()))
                .thenReturn(response);

        List<Employee> result = employeeService.getAllEmployees();

        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getEmployee_name());
    }

    @Test
    void testGetAllEmployees_nullBody() {
        ResponseEntity<ApiResponse<List<Employee>>> response = new ResponseEntity<>(null, HttpStatus.OK);

        when(restTemplate.exchange(
                        anyString(),
                        eq(HttpMethod.GET),
                        isNull(),
                        ArgumentMatchers.<ParameterizedTypeReference<ApiResponse<List<Employee>>>>any()))
                .thenReturn(response);

        List<Employee> result = employeeService.getAllEmployees();
        assertTrue(result.isEmpty());
    }

    @Test
    void testSearchByName_found() {
        Employee emp1 = createEmployee("1", "Alice", 5000);
        Employee emp2 = createEmployee("2", "Bob", 6000);

        EmployeeService spyService = Mockito.spy(employeeService);
        doReturn(Arrays.asList(emp1, emp2)).when(spyService).getAllEmployees();

        List<Employee> result = spyService.searchByName("ali");

        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).getEmployee_name());
    }

    @Test
    void testGetById_success() {
        Employee emp = createEmployee("1", "Alice", 5000);
        ApiResponse<Employee> apiResponse = new ApiResponse<>();
        apiResponse.setData(emp);

        ResponseEntity<ApiResponse<Employee>> response = new ResponseEntity<>(apiResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                        contains("/1"),
                        eq(HttpMethod.GET),
                        isNull(),
                        ArgumentMatchers.<ParameterizedTypeReference<ApiResponse<Employee>>>any()))
                .thenReturn(response);

        Employee result = employeeService.getById("1");
        assertNotNull(result);
        assertEquals("Alice", result.getEmployee_name());
    }

    @Test
    void testGetById_nullBody() {
        ResponseEntity<ApiResponse<Employee>> response = new ResponseEntity<>(null, HttpStatus.OK);

        when(restTemplate.exchange(
                        contains("/1"),
                        eq(HttpMethod.GET),
                        isNull(),
                        ArgumentMatchers.<ParameterizedTypeReference<ApiResponse<Employee>>>any()))
                .thenReturn(response);

        Employee result = employeeService.getById("1");
        assertNull(result);
    }

    @Test
    void testGetTop10HighestEarningEmployeeNames() {
        List<Employee> employees = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            employees.add(createEmployee(String.valueOf(i), "Emp" + i, 1000 + i));
        }

        EmployeeService spyService = Mockito.spy(employeeService);
        doReturn(employees).when(spyService).getAllEmployees();

        List<String> top10 = spyService.getTop10HighestEarningEmployeeNames();
        assertEquals(10, top10.size());
        assertTrue(top10.get(0).startsWith("Emp"));
    }

    @Test
    void testCreateEmployee_success() {
        Employee emp = createEmployee("1", "Alice", 5000);
        ApiResponse<Employee> apiResponse = new ApiResponse<>();
        apiResponse.setData(emp);

        ResponseEntity<ApiResponse<Employee>> response = new ResponseEntity<>(apiResponse, HttpStatus.CREATED);

        when(restTemplate.exchange(
                        anyString(),
                        eq(HttpMethod.POST),
                        any(HttpEntity.class),
                        ArgumentMatchers.<ParameterizedTypeReference<ApiResponse<Employee>>>any()))
                .thenReturn(response);

        EmployeeInput input = new EmployeeInput();
        input.setName("Alice");
        input.setSalary(5000);

        Employee result = employeeService.createEmployee(input);
        assertNotNull(result);
        assertEquals("Alice", result.getEmployee_name());
    }

    @Test
    void testCreateEmployee_nullBody() {
        ResponseEntity<ApiResponse<Employee>> response = new ResponseEntity<>(null, HttpStatus.OK);

        when(restTemplate.exchange(
                        anyString(),
                        eq(HttpMethod.POST),
                        any(HttpEntity.class),
                        ArgumentMatchers.<ParameterizedTypeReference<ApiResponse<Employee>>>any()))
                .thenReturn(response);

        EmployeeInput input = new EmployeeInput();
        Employee result = employeeService.createEmployee(input);

        assertNull(result);
    }

    @Test
    void testDeleteEmployeeById_success() {
        ApiResponse<Boolean> apiResponse = new ApiResponse<>();
        apiResponse.setData(true);

        ResponseEntity<ApiResponse<Boolean>> response = new ResponseEntity<>(apiResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                        contains("/1"),
                        eq(HttpMethod.DELETE),
                        isNull(),
                        ArgumentMatchers.<ParameterizedTypeReference<ApiResponse<Boolean>>>any()))
                .thenReturn(response);

        String result = employeeService.deleteEmployeeById("1");
        assertEquals("Employee with id 1 deleted successfully", result);
    }

    @Test
    void testDeleteEmployeeById_failure() {
        ApiResponse<Boolean> apiResponse = new ApiResponse<>();
        apiResponse.setData(false);

        ResponseEntity<ApiResponse<Boolean>> response = new ResponseEntity<>(apiResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                        contains("/1"),
                        eq(HttpMethod.DELETE),
                        isNull(),
                        ArgumentMatchers.<ParameterizedTypeReference<ApiResponse<Boolean>>>any()))
                .thenReturn(response);

        assertThrows(RuntimeException.class, () -> employeeService.deleteEmployeeById("1"));
    }
}
