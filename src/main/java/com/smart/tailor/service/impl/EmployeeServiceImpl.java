package com.smart.tailor.service.impl;

import com.smart.tailor.entities.Employee;
import com.smart.tailor.repository.EmployeeRepository;
import com.smart.tailor.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Override
    public Employee getByID(String empID) {
        if (empID == null) {
            return null;
        }
        var employee = employeeRepository.findById(empID);
        if (employee.isEmpty()) {
            return null;
        }
        return employee.get();
    }

    @Override
    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }
}
