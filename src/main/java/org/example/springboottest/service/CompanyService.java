package org.example.springboottest.service;

import org.example.springboottest.po.Company;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CompanyService {

    private final List<Company> companies = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    public Map<String, Object> create(Company company) {
        long id = idGenerator.incrementAndGet();
        company.setId(id);
        companies.add(company);
        return Map.of("id", company.getId());
    }

    public List<Company> list(Integer page, Integer size) {
        if (page != null && size != null && (page < 1 || size < 1)) {
            return List.of();
        }
        if (page == null || size == null) {
            return companies;
        }
        int fromIndex = (page - 1) * size;
        if (fromIndex >= companies.size()) {
            return List.of();
        }
        int toIndex = Math.min(fromIndex + size, companies.size());
        return companies.subList(fromIndex, toIndex);
    }

    public Company get(long id) {
        return companies.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Company update(long id, Company updatedCompany) {
        for (Company company : companies) {
            if (company.getId() == id) {
                company.setName(updatedCompany.getName());
                return company;
            }
        }
        return null;
    }

    public boolean delete(long id) {
        return companies.removeIf(company -> company.getId() == id);
    }
}
