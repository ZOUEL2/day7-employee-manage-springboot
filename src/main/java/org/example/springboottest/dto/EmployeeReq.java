package org.example.springboottest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeReq {
    private String name;
    private int age;
    private String gender;
    private double salary;
    private Long companyId;
}
