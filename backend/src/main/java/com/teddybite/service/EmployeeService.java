package com.teddybite.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teddybite.entity.Employee;
import com.teddybite.repository.EmployeeRepository;

public class EmployeeService implements IEmployeeService{
    
    @Autowired
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    
    @Override
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> findById(String id) {
        return employeeRepository.findById(id);
    }

    @Override
    public void deleteById(String id) {
        employeeRepository.deleteById(id); 
    }
}
