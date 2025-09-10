package org.example.springboottest.exception;

public class EmployeeIllegalAgeException extends RuntimeException{
    public EmployeeIllegalAgeException(String message) {
        super(message);
    }


}
