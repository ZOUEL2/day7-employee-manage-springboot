package org.example.springboottest.exception;

public class EmployeeDuplicateException extends RuntimeException {
    public EmployeeDuplicateException(String message) {
        super(message);
    }

}
