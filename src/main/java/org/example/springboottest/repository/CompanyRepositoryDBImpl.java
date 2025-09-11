package org.example.springboottest.repository;

import jakarta.annotation.Resource;
import org.example.springboottest.po.Company;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CompanyRepositoryDBImpl implements CompanyRepository {

    @Resource
    private CompanyJpaRepository companyJpaRepository;

    @Override
    public List<Company> listAll() {
        return companyJpaRepository.findAll();
    }

    @Override
    public void insert(Company company) {
        companyJpaRepository.save(company);
    }

    @Override
    public Company getById(long id) {
        return companyJpaRepository.findById(id).orElse(null);
    }

    @Override
    public boolean removeById(long id) {
        companyJpaRepository.deleteById(id);
        return true;
    }

    @Override
    public void updateById(Company updatedCompany) {
        companyJpaRepository.save(updatedCompany);

    }

    @Override
    public List<Company> listPage(Integer page, Integer size) {
        return companyJpaRepository.findAll(PageRequest.of(page, size)).toList();
    }

    @Override
    public void clear() {
        companyJpaRepository.deleteAll();
    }
}
