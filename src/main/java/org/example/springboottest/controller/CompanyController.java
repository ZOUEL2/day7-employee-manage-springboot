package org.example.springboottest.controller;

import lombok.RequiredArgsConstructor;
import org.example.springboottest.po.Company;
import org.example.springboottest.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {


    private final CompanyService companyService;


    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> createCompany(@RequestBody Company company) {
        return companyService.create(company);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Company> listCompanies(@RequestParam(required = false) Integer page,
                                       @RequestParam(required = false) Integer size) {
        return companyService.list(page, size);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable long id) {

        Company company = companyService.get(id);

        if (Objects.isNull(company)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(company);
    }

    // update company name by id
    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompanyName(@PathVariable long id, @RequestBody Company updatedCompany) {

        Company company = companyService.update(id, updatedCompany);

        if (Objects.isNull(company)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(company);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable long id) {
        boolean isRemoved = companyService.delete(id);
        if (isRemoved) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
