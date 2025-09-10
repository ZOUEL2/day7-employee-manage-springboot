package org.example.springboottest.service;

import org.example.springboottest.po.Employee;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class EmployeeService {

    public List<Employee> getEmployees() {
        return employees;
    }

    private final List<Employee> employees = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    public Map<String, Object> create(Employee employee) {
        long id = idGenerator.incrementAndGet();
        employee.setId(id);
        employees.add(employee);
        return Map.of("id", employee.getId());
    }


    public Employee getEmployee(long id) {
        return employees.stream().filter(employee -> employee.getId() == id)
                .findFirst()
                .orElse(null);
    }


    public List<Employee> queryList(String gender, Integer page, Integer size) {
        if (page != null && size != null && (page < 1 || size < 1)) {
            return List.of();
        }
        List<Employee> filteredEmployees;
        if (gender == null || gender.isBlank()) {
            filteredEmployees = employees;
        } else {
            String formatGender = gender.toLowerCase(Locale.ROOT);
            filteredEmployees = employees.stream()
                    .filter(e -> e.getGender() != null &&
                            e.getGender().toLowerCase(Locale.ROOT).equals(formatGender))
                    .toList();
        }

        if (page != null && size != null) {
            int fromIndex = (page - 1) * size;
            if (fromIndex >= filteredEmployees.size()) {
                return List.of();
            }
            int toIndex = Math.min(fromIndex + size, filteredEmployees.size());
            return filteredEmployees.subList(fromIndex, toIndex);
        }
        return filteredEmployees;
    }

    public Employee update(long id, Employee updatedEmployee) {
        return employees.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .map(e -> {
                    e.setName(updatedEmployee.getName());
                    e.setAge(updatedEmployee.getAge());
                    e.setGender(updatedEmployee.getGender());
                    e.setSalary(updatedEmployee.getSalary());
                    return updatedEmployee;
                })
                .orElse(null);
    }

    public boolean removeById(long id) {
       return employees.removeIf(e -> e.getId() == id);
    }
}
