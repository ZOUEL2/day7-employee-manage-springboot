package org.example.springboottest.repository;

import lombok.RequiredArgsConstructor;
import org.example.springboottest.po.Employee;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EmployeeRepositoryDBImpl implements EmployeeRepository {

    private final EmployeeJpaRepository employeeJpaRepository;

    @Override
    public void insert(Employee employee) {
        employeeJpaRepository.save(employee);
    }

    @Override
    public Employee findById(long id) {
        Optional<Employee> employee = employeeJpaRepository.findById(id);
        return employee.orElse(null);
    }

    @Override
    public void update(Employee updatedEmployee) {
        employeeJpaRepository.save(updatedEmployee);
    }

    @Override
    public List<Employee> listAll() {
        return employeeJpaRepository.findAll();
    }

    @Override
    public List<Employee> listByGender(String gender) {
        return employeeJpaRepository.findByGender(gender);
    }

    @Override
    public List<Employee> paginateAll(Integer page, Integer size) {
        return employeeJpaRepository.findAll(PageRequest.of(page - 1, size)).toList();
    }

    @Override
    public List<Employee> paginateByGender(String gender, Integer page, Integer size) {
        return employeeJpaRepository.findByGender(gender, PageRequest.of(page - 1, size));
    }

    @Override
    public void clear() {
        employeeJpaRepository.deleteAll();
    }


}
