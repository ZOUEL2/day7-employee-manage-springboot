package org.example.springboottest;

import org.example.springboottest.exception.EmployeeIllegalAgeException;
import org.example.springboottest.exception.EmployeeNotFoundException;
import org.example.springboottest.exception.EmployeeSalarySetException;
import org.example.springboottest.po.Employee;
import org.example.springboottest.repository.EmployeeRepository;
import org.example.springboottest.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Test
    public void should_throw_exception_when_create_employee_given_age_below_18_or_over_65() {
        Employee employee1 = new Employee();
        employee1.setName("sgesagv");
        employee1.setGender("Male");
        employee1.setAge(17);
        employee1.setSalary(100.0);

        Employee employee2 = new Employee();
        employee2.setName("sgesagv");
        employee2.setGender("Male");
        employee2.setAge(17);
        employee2.setSalary(100.0);


        assertThrows(EmployeeIllegalAgeException.class, () -> employeeService.create(employee1));

        assertThrows(EmployeeIllegalAgeException.class, () -> employeeService.create(employee2));
        verify(employeeRepository, never()).add(any());
    }

    @Test
    public void should_throw_exception_when_find_employee_by_illegal_id() {
        when(employeeRepository.findById(1)).thenReturn(null);
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.findById(1));
    }

    @Test
    public void should_throw_exception_when_create_employee_age_over_30_and_salary_below_20000() {
        Employee employee = new Employee();
        employee.setName("sgesagv");
        employee.setGender("Male");
        employee.setAge(37);
        employee.setSalary(100.0);
        assertThrows(EmployeeSalarySetException.class, () -> employeeService.create(employee));
    }


}
