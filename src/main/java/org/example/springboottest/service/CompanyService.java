package org.example.springboottest.service;

import org.example.springboottest.exception.CompanyNotFoundException;
import org.example.springboottest.po.Company;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CompanyService {

    private final List<Company> companies = new ArrayList<>();
    private final AtomicLong idGen = new AtomicLong(0);

    public Map<String, Object> create(Company company) {
        long id = idGen.incrementAndGet();
        company.setId(id);
        companies.add(company);
        return Map.of("id", id);
    }

    public Company get(long id) {
        return companies.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElseThrow(() -> new CompanyNotFoundException(""));
    }

    public List<Company> list(Integer page, Integer size) {
        if (page == null || size == null) {
            return companies;
        }
        if (page < 1 || size < 1) {
            return List.of();
        }
        int from = (page - 1) * size;
        if (from >= companies.size()) return List.of();
        int to = Math.min(from + size, companies.size());
        return companies.subList(from, to);
    }

    public void update(long id, Company updated) {
        Company existing = get(id);
        existing.setName(updated.getName());
    }

    public void delete(long id) {
        boolean removed = companies.removeIf(c -> c.getId() == id);
        if (!removed) {
            throw new CompanyNotFoundException("");
        }
    }
}
