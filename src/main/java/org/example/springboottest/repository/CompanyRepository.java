package org.example.springboottest.repository;

import org.example.springboottest.exception.CompanyNotFoundException;
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

    public Map<String, Object> insert(Company company) {
        long id = idGenerator.incrementAndGet();
        company.setId(id);
        companies.add(company);
        return Map.of("id", company.getId());
    }

    public Company getById(long id){
       return companies.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
               .orElse(null);
    }

    public boolean removeById(long id) {
        return companies.removeIf(company -> company.getId() == id);
    }

    public void updateById(Company updatedCompany) {
        for (Company company : companies) {
            if (company.getId() == updatedCompany.getId()) {
                company.setName(updatedCompany.getName());
            }
        }
    }

    public static List<Company> listAll() {
        return companies;
    }


    public List<Company> listPage(Integer page, Integer size) {
        int fromIndex = (page - 1) * size;
        if (fromIndex >= companies.size()) {
            return List.of();
        }
        int toIndex = Math.min(fromIndex + size, companies.size());
        return companies.subList(fromIndex, toIndex);
    }

    public void clear(){
        companies.clear();
    }
}
