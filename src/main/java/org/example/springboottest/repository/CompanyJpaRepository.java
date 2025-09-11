package org.example.springboottest.repository;

import org.example.springboottest.po.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyJpaRepository extends JpaRepository<Company, Long> {
}
