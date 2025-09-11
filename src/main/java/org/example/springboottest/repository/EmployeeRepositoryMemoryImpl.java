package org.example.springboottest.repository;

import org.example.springboottest.po.Employee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class EmployeeRepositoryMemoryImpl implements EmployeeRepository {

    private final List<Employee> employees = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    @Override
    public void insert(Employee employee) {
        long id = idGenerator.incrementAndGet();
        employee.setId(id);
        employees.add(employee);
    }

    @Override
    public Employee findById(long id) {
        return employees.stream().filter(employee -> employee.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void update(Employee updatedEmployee) {
        employees.stream()
                .filter(e -> e.getId() == updatedEmployee.getId())
                .findFirst()
                .map(e -> {
                    e.setName(updatedEmployee.getName());
                    e.setAge(updatedEmployee.getAge());
                    e.setGender(updatedEmployee.getGender());
                    e.setSalary(updatedEmployee.getSalary());
                    e.setCompanyId(updatedEmployee.getCompanyId());
                    e.setActiveStatus(updatedEmployee.isActiveStatus());
                    return null;
                });
    }

    @Override
    public List<Employee> listAll() {
        return employees;
    }

    @Override
    public List<Employee> listByGender(String gender) {
        String formatGender = gender.toLowerCase(Locale.ROOT);
        return employees.stream()
                .filter(e -> formatGender.equals(
                        Optional.ofNullable(e.getGender())
                                .map(g -> g.toLowerCase(Locale.ROOT))
                                .orElse(null)))
                .toList();
    }

    @Override
    public List<Employee> paginateAll(Integer page, Integer size) {
        int fromIndex = (page - 1) * size;
        if (fromIndex >= employees.size()) {
            return List.of();
        }
        int toIndex = Math.min(fromIndex + size, employees.size());
        return employees.subList(fromIndex, toIndex);
    }

    @Override
    public List<Employee> paginateByGender(String gender, Integer page, Integer size) {
        String formatGender = gender.toLowerCase(Locale.ROOT);
        List<Employee> filtered = employees.stream()
                .filter(e -> formatGender.equals(
                        Optional.ofNullable(e.getGender())
                                .map(g -> g.toLowerCase(Locale.ROOT))
                                .orElse(null)))
                .toList();
        int fromIndex = (page - 1) * size;
        if (fromIndex >= filtered.size()) {
            return List.of();
        }
        int toIndex = Math.min(fromIndex + size, filtered.size());
        return filtered.subList(fromIndex, toIndex);
    }

    @Override
    public void clear() {
        employees.clear();
    }


}
