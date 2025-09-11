package org.example.springboottest.repository;

import org.example.springboottest.po.Employee;

import java.util.List;

public interface EmployeeRepository {
    void insert(Employee employee);

    Employee findById(long id);

    void update(Employee updatedEmployee);

    List<Employee> listAll();

    List<Employee> listByGender(String gender);

    List<Employee> paginateAll(Integer page, Integer size);

    List<Employee> paginateByGender(String gender, Integer page, Integer size);

}
