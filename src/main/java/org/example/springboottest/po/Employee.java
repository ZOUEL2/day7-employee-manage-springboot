package org.example.springboottest.po;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Employee {

    private long id;
    private String name;
    private int age;
    private String gender;
    private double salary;
    private boolean status = true;

    public Employee() {
    }

    public Employee(String name, int age, String gender, double salary) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.salary = salary;
    }
}
