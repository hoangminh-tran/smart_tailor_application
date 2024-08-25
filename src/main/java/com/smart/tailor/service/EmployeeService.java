package com.smart.tailor.service;

import com.smart.tailor.entities.Employee;

import java.util.List;


public interface EmployeeService {
    Employee getByID(String empID);

    List<Employee> getAll();
}
