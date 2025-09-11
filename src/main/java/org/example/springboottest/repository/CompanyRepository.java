package org.example.springboottest.repository;

import org.example.springboottest.po.Company;

import java.util.List;

public interface CompanyRepository {
    List<Company> listAll();

    void insert(Company company);

    Company getById(long id);

    boolean removeById(long id);

    void updateById(Company updatedCompany);

    List<Company> listPage(Integer page, Integer size);

    void clear();
}
