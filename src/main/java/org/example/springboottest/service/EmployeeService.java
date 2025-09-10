package org.example.springboottest.service;

import lombok.RequiredArgsConstructor;
import org.example.springboottest.constants.EmployeeExceptionMessage;
import org.example.springboottest.exception.EmployeeDuplicateException;
import org.example.springboottest.exception.EmployeeIllegalAgeException;
import org.example.springboottest.exception.EmployeeNotFoundException;
import org.example.springboottest.exception.EmployeeSalarySetException;
import org.example.springboottest.po.Employee;
import org.example.springboottest.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;


    public Map<String, Object> create(Employee employee) {
        boolean duplicate = employeeRepository.listAll().stream()
                .anyMatch(e -> e.isStatus()
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
        if (employee.getAge() > 30 && employee.getSalary() < 20000.0) {
            throw new EmployeeSalarySetException(EmployeeExceptionMessage.ILLEGAL_SALARY);
        }
        employeeRepository.insert(employee);
        return Map.of("id", employee.getId(), "status", employee.isStatus());
    }


    public Employee findById(long id) {
        Employee employee = employeeRepository.findById(id);
        if (employee == null){
            throw new EmployeeNotFoundException(EmployeeExceptionMessage.EMPLOYEE_NOT_FOUND);
        }
        return employee;
    }


    public List<Employee> queryList(String gender, Integer page, Integer size) {
        List<Employee> filtered = (gender == null)
                ? employeeRepository.listAll()
                : employeeRepository.listByGender(gender);

        if (page == null || size == null) {
            return filtered;
        }
        if (page < 1 || size < 1) {
            return List.of();
        }
        return employeeRepository.paginate(filtered, page, size);
    }


    public void update(long id, Employee updatedEmployee) {
        Employee existing = employeeRepository.findById(id);
        if (existing == null || !existing.isStatus()) {
            throw new EmployeeNotFoundException(EmployeeExceptionMessage.EMPLOYEE_NOT_FOUND);
        }
        employeeRepository.update(id, updatedEmployee);
    }

    public void removeById(long id) {
        Employee employee = employeeRepository.findById(id);
        if (employee == null ) {
            throw new EmployeeNotFoundException(EmployeeExceptionMessage.EMPLOYEE_NOT_FOUND);
        }
        if (!employee.isStatus()) {
            return;
        }
        employee.setStatus(false);
        employeeRepository.update(id, employee);
    }
}
