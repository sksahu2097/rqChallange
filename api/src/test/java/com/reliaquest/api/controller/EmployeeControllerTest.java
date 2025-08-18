package com.reliaquest.api.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeInput;
import com.reliaquest.api.service.EmployeeService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }

    public Employee createEmployee(String id, String name, int salary, int age, String occuption, String email) {
        Employee emp = new Employee();
        emp.setId(id);
        emp.setEmployee_age(age);
        emp.setEmployee_email(email);
        emp.setEmployee_salary(salary);
        emp.setEmployee_title(occuption);
        emp.setEmployee_name(name);
        return emp;
    }

    public EmployeeInput createEmployeeInput(String name, int salary, int age, String occuption) {
        EmployeeInput emp = new EmployeeInput();
        emp.setName(name);
        emp.setTitle(occuption);
        emp.setAge(age);
        emp.setSalary(salary);
        return emp;
    }

    @Test
    void testGetAllEmployees() throws Exception {
        List<Employee> employees = Arrays.asList(createEmployee("1", "Ravi", 500, 25, "Engineer", "ravi@mail.com"));
        when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].employee_name").value("Ravi"))
                .andExpect(jsonPath("$[0].employee_salary").value(500));

        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    void testGetEmployeeById() throws Exception {
        Employee emp = createEmployee("2", "Jane", 6000, 25, "Engineer", "jane@mail.com");
        when(employeeService.getById("2")).thenReturn(emp);

        mockMvc.perform(get("/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("2"))
                .andExpect(jsonPath("$.employee_name").value("Jane"))
                .andExpect(jsonPath("$.employee_salary").value(6000));

        verify(employeeService, times(1)).getById("2");
    }

    @Test
    void testSearchEmployeesByName() throws Exception {
        List<Employee> employees = Arrays.asList(createEmployee("3", "Alice", 7000, 30, "Lead", "meera@mail.com"));
        when(employeeService.searchByName("Alice")).thenReturn(employees);

        mockMvc.perform(get("/search/Alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].employee_name").value("Alice"));

        verify(employeeService, times(1)).searchByName("Alice");
    }

    @Test
    void testGetHighestSalary() throws Exception {
        when(employeeService.getHighestSalary()).thenReturn(10000);

        mockMvc.perform(get("/highestSalary"))
                .andExpect(status().isOk())
                .andExpect(content().string("10000"));

        verify(employeeService, times(1)).getHighestSalary();
    }

    @Test
    void testGetTopTenHighestEarningEmployeeNames() throws Exception {
        List<String> names = Arrays.asList("John", "Jane", "Alice");
        when(employeeService.getTop10HighestEarningEmployeeNames()).thenReturn(names);

        mockMvc.perform(get("/topTenHighestEarningEmployeeNames"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("John"))
                .andExpect(jsonPath("$[1]").value("Jane"))
                .andExpect(jsonPath("$[2]").value("Alice"));

        verify(employeeService, times(1)).getTop10HighestEarningEmployeeNames();
    }

    @Test
    void testCreateEmployee() throws Exception {
        EmployeeInput input = createEmployeeInput("Bob", 8000, 30, "Lead");
        Employee created = createEmployee("5", "Bob", 8000, 30, "Lead", "bob@mail.com");

        when(employeeService.createEmployee(any(EmployeeInput.class))).thenReturn(created);

        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Bob\", \"salary\":8000, \"age\":30, \"title\":\"Lead\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("5"))
                .andExpect(jsonPath("$.employee_name").value("Bob"))
                .andExpect(jsonPath("$.employee_salary").value(8000))
                .andExpect(jsonPath("$.employee_age").value(30))
                .andExpect(jsonPath("$.employee_title").value("Lead"));

        verify(employeeService, times(1)).createEmployee(any(EmployeeInput.class));
    }

    @Test
    void testDeleteEmployeeById() throws Exception {
        when(employeeService.deleteEmployeeById("10")).thenReturn("Employee deleted successfully");

        mockMvc.perform(delete("/10"))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee deleted successfully"));

        verify(employeeService, times(1)).deleteEmployeeById("10");
    }
}
