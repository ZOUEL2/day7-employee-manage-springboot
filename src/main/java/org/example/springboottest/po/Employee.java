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

    public Employee(int id, String name, int age, String gender, double salary) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.salary = salary;
    }

    public Employee() {
    }

}
