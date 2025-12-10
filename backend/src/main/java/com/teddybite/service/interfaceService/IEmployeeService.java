package com.teddybite.service.interfaceService;

import java.util.Optional;
import java.util.List;

import com.teddybite.dto.EmployeeCreateDTO;
import com.teddybite.entity.Employee;

public interface IEmployeeService {

    public Employee createEmployee(EmployeeCreateDTO employeeDTO);

    public List<Employee> readEmployeeAll();

    public Optional<Employee> readEmployeeById(String id);

    public Employee updateEmployee(Employee employee);

    public void deleteEmployeeById(String id);

    public void deleteEmployees(List<String> ids);

}
