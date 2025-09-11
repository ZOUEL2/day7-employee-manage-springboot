package org.example.springboottest.po;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "t_company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    @OneToMany(cascade =CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private List<Employee> employees = new ArrayList<>();
}
