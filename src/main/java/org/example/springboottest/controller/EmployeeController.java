package org.example.springboottest.controller;

import org.example.springboottest.po.Employee;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final List<Employee> employees = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    @PostMapping
    public ResponseEntity<Map<String, Object>> createEmployee(@RequestBody Employee employee) {
        long id = idGenerator.incrementAndGet();
        employee.setId(id);
        employees.add(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", employee.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable long id) {
        return employees.stream().filter(employee -> employee.getId() == id)
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    public ResponseEntity<List<Employee>> queryEmployeeByGender(
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        if (page != null && size != null && (page < 1 || size < 1)) {
            return ResponseEntity.ok(List.of());
        }

        List<Employee> filtered;
        if (gender == null || gender.isBlank()) {
            filtered = employees;
        } else {
            String formatGender = gender.toLowerCase(Locale.ROOT);
            filtered = employees.stream()
                    .filter(e -> e.getGender() != null &&
                            e.getGender().toLowerCase(Locale.ROOT).equals(formatGender))
                    .toList();
        }

        if (page != null && size != null) {
            int fromIndex = (page - 1) * size;
            if (fromIndex >= filtered.size()) {
                return ResponseEntity.ok(List.of());
            }
            int toIndex = Math.min(fromIndex + size, filtered.size());
            return ResponseEntity.ok(filtered.subList(fromIndex, toIndex));
        }

        return ResponseEntity.ok(filtered);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateEmployee(@PathVariable long id, @RequestBody Employee updatedEmployee) {
        return employees.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .map(e -> {
                    e.setName(updatedEmployee.getName());
                    e.setAge(updatedEmployee.getAge());
                    e.setGender(updatedEmployee.getGender());
                    e.setSalary(updatedEmployee.getSalary());
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable long id) {
        boolean removed = employees.removeIf(e -> e.getId() == id);
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
