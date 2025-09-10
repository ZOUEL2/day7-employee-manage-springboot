package org.example.springboottest;

import org.example.springboottest.exception.EmployeeIllegalAgeException;
import org.example.springboottest.exception.EmployeeNotFoundException;
import org.example.springboottest.exception.EmployeeSalarySetException;
import org.example.springboottest.po.Employee;
import org.example.springboottest.repository.EmployeeRepository;
import org.example.springboottest.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

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
        verify(employeeRepository, never()).insert(any());
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

    @Test
    public void should_throw_exception_when_update_employee_given_not_exist_id() {
        Employee updated = new Employee();
        updated.setName("New");
        when(employeeRepository.update(100L, updated)).thenReturn(null);
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.update(100L, updated));
    }

    @Test
    public void should_throw_exception_when_update_employee_given_inactive_employee() {
        Employee inactive = new Employee("Old",30,"Male",5000);
        inactive.setId(200L);
        inactive.setStatus(false);
        when(employeeRepository.findById(200L)).thenReturn(inactive);

        Employee updated = new Employee("NewName",31,"Male",6000);
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.update(200L, updated));
        verify(employeeRepository, never()).update(anyLong(), any());
    }

    @Test
    public void should_throw_exception_when_delete_employee_given_not_exist_id() {
        when(employeeRepository.findById(999L)).thenReturn(null);
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.removeById(999L));
        verify(employeeRepository, never()).update(anyLong(), any());
    }

    @Test
    public void should_invoke_repository_remove_when_delete_employee_given_existing_id() {
        Employee existing = new Employee("Jack", 25, "Male", 5000.0);
        existing.setId(10L);
        existing.setStatus(true);

        when(employeeRepository.findById(10L)).thenReturn(existing);
        when(employeeRepository.update(eq(10L), any(Employee.class)))
                .thenAnswer(inv -> inv.getArgument(1));

        employeeService.removeById(10L);

        verify(employeeRepository).update(eq(10L), employeeArgumentCaptor.capture());
        assertFalse(employeeArgumentCaptor.getValue().isStatus());
    }

    @Test
    void should_default_employee_status_true_when_create_given_valid_body(){
        Employee employee = new Employee("Tom", 20,  "Male",800.0);
        employeeService.create(employee);
        verify(employeeRepository,times(1)).insert(employeeArgumentCaptor.capture());
        Employee value = employeeArgumentCaptor.getValue();
        assertTrue(value.isStatus());
    }
}
