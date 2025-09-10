package org.example.springboottest.service;

import lombok.RequiredArgsConstructor;
import org.example.springboottest.exception.EmployeeIllegalAgeException;
import org.example.springboottest.exception.EmployeeNotFoundException;
import org.example.springboottest.exception.EmployeeSalarySetException;
import org.example.springboottest.po.Employee;
import org.example.springboottest.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;


    public Map<String, Object> create(Employee employee) {
        if (employee.getAge() < 18 || employee.getAge() > 65) {
            throw new EmployeeIllegalAgeException("");
        }
        if (employee.getAge() > 30 && employee.getSalary() < 20000.0) {
            throw new EmployeeSalarySetException("");
        }
        employeeRepository.add(employee);
        return Map.of("id", employee.getId());
    }


    public Employee findById(long id) {
        Employee employee = employeeRepository.findById(id);
        if (employee == null){
            throw new EmployeeNotFoundException("");
        }
        return employee;
    }


    public List<Employee> queryList(String gender, Integer page, Integer size) {
        if (gender == null && (page == null && size == null)) {
            return employeeRepository.listAll();
        }

        if (gender != null && (page == null && size == null)) {
            return employeeRepository.listByGender(gender);
        }

        if (gender == null && page != null && size != null) {
            if (page < 1 || size < 1) {
                return List.of();
            }
            return employeeRepository.listPage(page, size);
        }
        employeeRepository.listByGender(gender);
        return employeeRepository.listPage(page, size);
    }


    public Employee update(long id, Employee updatedEmployee) {
        return employeeRepository.update(id, updatedEmployee);
    }

    public boolean removeById(long id) {
        return employeeRepository.remove(id);
    }
}
