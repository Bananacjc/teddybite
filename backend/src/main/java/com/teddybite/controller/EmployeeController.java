package com.teddybite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import com.teddybite.entity.Employee;
import com.teddybite.service.EmployeeService;
import com.teddybite.service.IEmployeeService;


@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    
    @Autowired
    private final IEmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<Employee> save(@RequestBody Employee employee) {
        Employee savedEmployee = employeeService.save(employee);

        URI location = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(savedEmployee.getEmployeeID())
        .toUri();

        return ResponseEntity.created(location).body(savedEmployee);
    }

    @GetMapping
    public List<Employee> findAll() {
        return employeeService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> findById(@PathVariable String id) {
        return ResponseEntity.of(employeeService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable String id, @RequestBody Employee employeeDetails) {
        return employeeService.findById(id)
        .map(currentEmployee -> {
            currentEmployee.setName(employeeDetails.getName());
            currentEmployee.setGender(employeeDetails.getGender());
            currentEmployee.setDOB(employeeDetails.getDOB());
            currentEmployee.setContactNo(employeeDetails.getContactNo());
            currentEmployee.setEmail(employeeDetails.getEmail());
            currentEmployee.setPosition(employeeDetails.getPosition());

            Employee updatEmployee = employeeService.save(currentEmployee);

            return ResponseEntity.ok(updatEmployee);
        })
        .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        if (employeeService.findById(id).isPresent()) {
            employeeService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
}
