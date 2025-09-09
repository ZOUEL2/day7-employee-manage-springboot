package org.example.springboottest.controller;

import org.example.springboottest.po.Company;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final List<Company> companies = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(0);


    @PostMapping
    public ResponseEntity<Map<String, Object>> createCompany(@RequestBody Company company) {
        long id = idGenerator.incrementAndGet();
        company.setId(id);
        companies.add(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", company.getId()));
    }

    @GetMapping
    public List<Company> listCompanies() {
        return companies;
    }


}
