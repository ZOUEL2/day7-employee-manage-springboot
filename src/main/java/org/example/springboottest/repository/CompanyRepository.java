package org.example.springboottest.repository;

import org.example.springboottest.po.Company;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class CompanyRepository {

    private static final List<Company> companies = new ArrayList<>();

    private final AtomicLong idGenerator = new AtomicLong(0);

    public static List<Company> listAll() {
        return companies;
    }

    public Map<String, Object> add(Company company) {
        long id = idGenerator.incrementAndGet();
        company.setId(id);
        companies.add(company);
        return Map.of("id", company.getId());
    }

    public boolean removeById(long id) {
        return companies.removeIf(company -> company.getId() == id);
    }

    public Company updateById(long id, Company updatedCompany) {
        for (Company company : companies) {
            if (company.getId() == id) {
                company.setName(updatedCompany.getName());
                return company;
            }
        }
        return null;
    }

    public List<Company> listPage(Integer page, Integer size) {
        int fromIndex = (page - 1) * size;
        if (fromIndex >= companies.size()) {
            return List.of();
        }
        int toIndex = Math.min(fromIndex + size, companies.size());
        return companies.subList(fromIndex, toIndex);
    }
}
