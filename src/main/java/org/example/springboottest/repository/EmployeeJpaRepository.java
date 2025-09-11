package org.example.springboottest.repository;

import org.example.springboottest.po.Employee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeJpaRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByGender(String gender);

    List<Employee> findByGender(String gender, Pageable pageable);
}
