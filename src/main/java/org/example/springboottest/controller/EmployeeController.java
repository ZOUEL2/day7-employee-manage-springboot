package org.example.springboottest.controller;

import org.example.springboottest.po.Employee;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    List<Employee> employees = new ArrayList<>();

    @PostMapping
    public ResponseEntity<Map<String, Object>> createEmployee(@RequestBody Employee employee) {
        employee.setId(employees.size() + 1);
        employees.add(employee);
        return ResponseEntity.status(201).body(Map.of("id", employee.getId()));
    }

    @GetMapping("/{id}")
    public Employee getEmployee(@PathVariable long id) {
        return employees.stream().filter(employee -> employee.getId() == id).findFirst().orElse(null);
    }

    @GetMapping
    public List<Employee> queryEmployeeByGender(@RequestParam String gender) {
        return employees.stream().filter(employee -> employee.getGender() == gender).toList();
    }
}
