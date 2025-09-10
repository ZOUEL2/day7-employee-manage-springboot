package org.example.springboottest.service;

import lombok.RequiredArgsConstructor;
import org.example.springboottest.po.Company;
import org.example.springboottest.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final List<Company> companies = new ArrayList<>();

    private final CompanyRepository companyRepository;

    public Map<String, Object> create(Company company) {
        return companyRepository.add(company);
    }

    public List<Company> list(Integer page, Integer size) {
        if (page != null && size != null && (page < 1 || size < 1)) {
            return List.of();
        }
        if (page == null || size == null) {
            return CompanyRepository.listAll();
        }
       return companyRepository.listPage(page,size);
    }

    public Company get(long id) {
        return companies.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Company update(long id, Company updatedCompany) {
        return companyRepository.updateById(id,updatedCompany);
    }

    public boolean delete(long id) {
        return companyRepository.removeById(id);
    }
}
