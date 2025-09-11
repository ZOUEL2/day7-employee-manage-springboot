package org.example.springboottest;

import org.example.springboottest.exception.EmployeeDuplicateException;
import org.example.springboottest.exception.EmployeeIllegalAgeException;
import org.example.springboottest.exception.EmployeeNotFoundException;
import org.example.springboottest.exception.EmployeeSalarySetException;
import org.example.springboottest.po.Employee;
import org.example.springboottest.repository.EmployeeRepositoryMemoryImpl;
import org.example.springboottest.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepositoryMemoryImpl employeeRepositoryMemoryImpl;

    @Captor
    private ArgumentCaptor<Employee> employeeArgumentCaptor;

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
        employee2.setAge(67);
        employee2.setSalary(100.0);

        assertThrows(EmployeeIllegalAgeException.class, () -> employeeService.create(employee1));
        assertThrows(EmployeeIllegalAgeException.class, () -> employeeService.create(employee2));
        verify(employeeRepositoryMemoryImpl, never()).insert(any());
    }

    @Test
    public void should_throw_exception_when_find_employee_by_illegal_id() {
        when(employeeRepositoryMemoryImpl.findById(1)).thenReturn(null);
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

    @Test
    public void should_throw_exception_when_update_employee_given_not_exist_id() {
        Employee updated = new Employee();
        updated.setId(100L);
        updated.setName("New");

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.update(updated));
    }

    @Test
    public void should_throw_exception_when_update_employee_given_inactive_employee() {
        Employee inactive = new Employee("Old", 30, "Male", 5000);
        inactive.setId(200L);
        inactive.setActiveStatus(false);
        
        Employee updated = new Employee("NewName", 31, "Male", 6000);
        updated.setId(200L);
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.update(updated));
        verify(employeeRepositoryMemoryImpl, never()).update(any());
    }

    @Test
    public void should_throw_exception_when_delete_employee_given_not_exist_id() {
        when(employeeRepositoryMemoryImpl.findById(999L)).thenReturn(null);
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.removeById(999L));
        verify(employeeRepositoryMemoryImpl, never()).update(any());
    }

    @Test
    public void should_invoke_repository_remove_when_delete_employee_given_existing_id() {
        Employee existing = new Employee("Jack", 25, "Male", 5000.0);
        existing.setId(10L);
        existing.setActiveStatus(true);

        when(employeeRepositoryMemoryImpl.findById(10L)).thenReturn(existing);

        employeeService.removeById(10L);

        verify(employeeRepositoryMemoryImpl).update(employeeArgumentCaptor.capture());
        assertFalse(employeeArgumentCaptor.getValue().isActiveStatus());
    }

    @Test
    void should_default_employee_status_true_when_create_given_valid_body() {
        Employee employee = new Employee("Tom", 20, "Male", 800.0);
        employeeService.create(employee);
        verify(employeeRepositoryMemoryImpl, times(1)).insert(employeeArgumentCaptor.capture());
        Employee value = employeeArgumentCaptor.getValue();
        assertTrue(value.isActiveStatus());
    }

    @Test
    public void should_throw_exception_when_create_employee_with_duplicate_name_and_gender() {
        Employee existing = new Employee("Tom", 25, "Male", 30000);
        existing.setId(1L);
        existing.setActiveStatus(true);

        when(employeeRepositoryMemoryImpl.listAll()).thenReturn(List.of(existing));

        Employee duplicate = new Employee("Tom", 26, "Male", 40000);

        assertThrows(EmployeeDuplicateException.class, () -> employeeService.create(duplicate));
        verify(employeeRepositoryMemoryImpl, never()).insert(any());
    }
}
