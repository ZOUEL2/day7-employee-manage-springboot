package org.example.springboottest.controller;

import lombok.RequiredArgsConstructor;
import org.example.springboottest.po.Employee;
import org.example.springboottest.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> createEmployee(@RequestBody Employee employee) {
        return employeeService.create(employee);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable long id) {
        return ResponseEntity.ok(employeeService.findById(id));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Employee> queryEmployeeList(
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        return employeeService.queryList(gender, page, size);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateEmployee(@PathVariable long id, @RequestBody Employee updatedEmployee) {
        updatedEmployee.setId(id);
        employeeService.update(updatedEmployee);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable long id) {
        employeeService.removeById(id);
        return ResponseEntity.noContent().build();
    }
}
