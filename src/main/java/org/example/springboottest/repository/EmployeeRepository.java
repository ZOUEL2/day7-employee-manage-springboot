package org.example.springboottest.repository;

import org.example.springboottest.po.Employee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class EmployeeRepository {

    private final List<Employee> employees = new ArrayList<>();

    private final AtomicLong idGenerator = new AtomicLong(0);

    public void add(Employee employee) {
        long id = idGenerator.incrementAndGet();
        employee.setId(id);
        employees.add(employee);
    }

    public Employee findById(long id) {
        return employees.stream().filter(employee -> employee.getId() == id)
                .findFirst()
                .orElse(null);
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

    public boolean remove(long id) {
        return employees.removeIf(e -> e.getId() == id);
    }

    public List<Employee> listAll() {
        return employees;
    }

    public List<Employee> listByGender(String gender) {
        String formatGender = gender.toLowerCase(Locale.ROOT);
        return employees.stream()
                .filter(e -> formatGender.equals(
                        Optional.ofNullable(e.getGender())
                                .map(g -> g.toLowerCase(Locale.ROOT))
                                .orElse(null)))
                .toList();
    }

    public List<Employee> listPage(Integer page, Integer size) {
        int fromIndex = (page - 1) * size;
        if (fromIndex >= employees.size()) {
            return List.of();
        }
        int toIndex = Math.min(fromIndex + size, employees.size());
        return employees.subList(fromIndex, toIndex);
    }


}
