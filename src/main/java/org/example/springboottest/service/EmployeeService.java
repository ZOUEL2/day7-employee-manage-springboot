package org.example.springboottest.service;

import jakarta.annotation.Resource;
import org.example.springboottest.constants.EmployeeExceptionMessage;
import org.example.springboottest.exception.EmployeeDuplicateException;
import org.example.springboottest.exception.EmployeeIllegalAgeException;
import org.example.springboottest.exception.EmployeeNotFoundException;
import org.example.springboottest.exception.EmployeeSalarySetException;
import org.example.springboottest.po.Employee;
import org.example.springboottest.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {

    @Qualifier("employeeRepositoryDBImpl")
    @Resource
    private EmployeeRepository employeeRepository;

    public Map<String, Object> create(Employee employee) {
        boolean duplicate = employeeRepository.listAll().stream()
                .anyMatch(e -> e.isActiveStatus()
                        && e.getName() != null && e.getGender() != null
                        && employee.getName() != null && employee.getGender() != null
                        && e.getName().equalsIgnoreCase(employee.getName())
                        && e.getGender().equalsIgnoreCase(employee.getGender()));
        if (duplicate) {
            throw new EmployeeDuplicateException(EmployeeExceptionMessage.EMPLOYEE_DUPLICATE);
        }
        if (employee.getAge() < 18 || employee.getAge() > 65) {
            throw new EmployeeIllegalAgeException(EmployeeExceptionMessage.ILLEGAL_AGE);
        }
        if (employee.getAge() >= 30 && employee.getSalary() < 20000.0) {
            throw new EmployeeSalarySetException(EmployeeExceptionMessage.ILLEGAL_SALARY);
        }
        employeeRepository.insert(employee);
        return Map.of("id", employee.getId(), "status", employee.isActiveStatus());
    }


    public Employee findById(long id) {
        Employee employee = employeeRepository.findById(id);
        if (employee == null) {
            throw new EmployeeNotFoundException(EmployeeExceptionMessage.EMPLOYEE_NOT_FOUND);
        }
        return employee;
    }


    public List<Employee> queryList(String gender, Integer page, Integer size) {
        boolean needPaging = page != null && size != null;
        if (needPaging && (page < 1 || size < 1)) {
            return List.of();
        }

        if (gender == null || gender.isBlank()) {
            if (needPaging) {
                return employeeRepository.paginateAll(page, size);
            } else {
                return employeeRepository.listAll();
            }
        } else {
            if (needPaging) {
                return employeeRepository.paginateByGender(gender, page, size);
            } else {
                return employeeRepository.listByGender(gender);
            }
        }
    }

    public void update(Employee updatedEmployee) {
        Employee existing = employeeRepository.findById(updatedEmployee.getId());
        if (existing == null || !existing.isActiveStatus()) {
            throw new EmployeeNotFoundException(EmployeeExceptionMessage.EMPLOYEE_NOT_FOUND);
        }
        employeeRepository.update(updatedEmployee);
    }

    public void removeById(long id) {
        Employee employee = employeeRepository.findById(id);
        if (employee == null) {
            throw new EmployeeNotFoundException(EmployeeExceptionMessage.EMPLOYEE_NOT_FOUND);
        }
        if (!employee.isActiveStatus()) {
            return;
        }
        employee.setActiveStatus(false);
        employeeRepository.update(employee);
    }
}
