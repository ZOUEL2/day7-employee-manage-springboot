package org.example.springboottest.service;

import jakarta.annotation.Resource;
import org.example.springboottest.exception.CompanyNotFoundException;
import org.example.springboottest.po.Company;
import org.example.springboottest.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CompanyService {

    @Resource
    private CompanyRepository companyRepository;

    public Map<String, Object> create(Company company) {
        return companyRepository.insert(company);
    }

    public Company get(long id) {
        Company company = companyRepository.getById(id);
        if (company == null) {
            throw new CompanyNotFoundException("no exist id:" + id + "company");
        }
        return company;
    }

    public List<Company> list(Integer page, Integer size) {
        boolean needPaging = page != null && size != null;
        if (needPaging && (page < 1 || size < 1)) {
            return List.of();
        }

        if (!needPaging) {
            return companyRepository.listAll();
        }

        return companyRepository.listPage(page, size);
    }

    public void update(Company updated) {
        Company company = companyRepository.getById(updated.getId());
        if (company == null) {
            throw new CompanyNotFoundException("no exist id:" + updated.getId() + "company");
        }
        companyRepository.updateById(updated);
    }

    public void delete(long id) {
        boolean removed = companyRepository.removeById(id);
        if (!removed) {
            throw new CompanyNotFoundException("no exist id:" + id + "company");
        }
    }
}
