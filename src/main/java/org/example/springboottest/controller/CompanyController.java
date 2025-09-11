package org.example.springboottest.controller;

import lombok.RequiredArgsConstructor;
import org.example.springboottest.po.Company;
import org.example.springboottest.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public java.util.Map<String, Object> createCompany(@RequestBody Company company) {
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
        return ResponseEntity.ok(companyService.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCompanyName(@PathVariable long id, @RequestBody Company updatedCompany) {
        updatedCompany.setId(id);
        companyService.update(updatedCompany);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable long id) {
        companyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
