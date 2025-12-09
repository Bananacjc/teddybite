package com.teddybite.service;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.teddybite.dto.EmployeeCreateDTO;
import com.teddybite.entity.Employee;
import com.teddybite.repository.EmployeeRepository;

@Service
public class EmployeeService implements IEmployeeService{
    
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    
    public EmployeeService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public Employee createEmployee(EmployeeCreateDTO employeeDTO) {
        Employee newEmployee = new Employee();

        newEmployee.setName(employeeDTO.getName());
        newEmployee.setGender(employeeDTO.getGender());
        newEmployee.setDOB(employeeDTO.getDOB());
        newEmployee.setContactNo(employeeDTO.getContactNo());
        newEmployee.setEmail(employeeDTO.getEmail());
        newEmployee.setPosition(employeeDTO.getPosition());
        newEmployee.setSalary(employeeDTO.getPosition().salary);
        newEmployee.setDateJoined(LocalDateTime.now());

        newEmployee.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));

        return employeeRepository.save(newEmployee);
    }

    @Override
    public List<Employee> readEmployeeAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> readEmployeeById(String id) {
        return employeeRepository.findById(id);
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployeeById(String id) {
        employeeRepository.deleteById(id); 
    }
}
