package com.teddybite.service;

import java.util.Optional;
import java.util.List;

import com.teddybite.entity.Employee;

public interface IEmployeeService {
    
    public Employee save(Employee employee);
    public List<Employee> findAll();
    public Optional<Employee> findById(String id);
    public void deleteById(String id);

}
