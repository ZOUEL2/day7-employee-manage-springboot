package org.example.springboottest.controller;

import org.example.springboottest.po.Company;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    public ResponseEntity<List<Company>> listCompanies(@RequestParam(required = false) Integer page,
                                                       @RequestParam(required = false) Integer size) {
        if (page != null && size != null && (page < 1 || size < 1)) {
            return ResponseEntity.ok(List.of());
        }
        if (page == null || size == null) {
            return ResponseEntity.ok(companies);
        }
        int fromIndex = (page - 1) * size;
        if (fromIndex >= companies.size()) {
            return ResponseEntity.ok(List.of());
        }
        int toIndex = Math.min(fromIndex + size, companies.size());
        return ResponseEntity.ok(companies.subList(fromIndex, toIndex));
    }


    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable long id) {
        Optional<Company> company = companies.stream()
                .filter(c -> c.getId() == id)
                .findFirst();
        return company.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // update company name by id
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCompanyName(@PathVariable long id, @RequestBody Company updatedCompany) {
        for (Company company : companies) {
            if (company.getId() == id) {
                company.setName(updatedCompany.getName());
                return ResponseEntity.noContent().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable long id) {
        boolean removed = companies.removeIf(company -> company.getId() == id);
        if (removed) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
