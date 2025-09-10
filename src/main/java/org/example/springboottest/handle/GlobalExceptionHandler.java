package org.example.springboottest.handle;

import org.example.springboottest.exception.EmployeeIllegalAgeException;
import org.example.springboottest.exception.EmployeeNotFoundException;
import org.example.springboottest.exception.EmployeeSalarySetException;
import org.example.springboottest.exception.CompanyNotFoundException;
import org.example.springboottest.exception.EmployeeDuplicateException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmployeeIllegalAgeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleEmployeeNotAmongLegalAgeException(Exception e){
        return e.getMessage();
    }

    @ExceptionHandler(EmployeeSalarySetException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleEmployeeSalarySetException(Exception e){
        return e.getMessage();
    }

    @ExceptionHandler(EmployeeDuplicateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleEmployeeDuplicateException(Exception e){
        return e.getMessage();
    }

    @ExceptionHandler({EmployeeNotFoundException.class, CompanyNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleNotFound(Exception e){
    }


}
