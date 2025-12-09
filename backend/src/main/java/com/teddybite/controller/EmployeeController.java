package com.teddybite.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;

import com.teddybite.dto.EmployeeCreateDTO;
import com.teddybite.dto.EmployeeUpdateDTO;
import com.teddybite.entity.Employee;
import com.teddybite.service.IEmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final IEmployeeService employeeService;

    public EmployeeController(IEmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<Employee> save(@Valid @RequestBody EmployeeCreateDTO employeeDTO) {

        Employee savedEmployee = employeeService.createEmployee(employeeDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedEmployee.getEmployeeID())
                .toUri();

        return ResponseEntity.created(location).body(savedEmployee);
    }

    @GetMapping
    public List<Employee> findAll() {
        return employeeService.readEmployeeAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> findById(@PathVariable String id) {
        return ResponseEntity.of(employeeService.readEmployeeById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable String id, @Valid @RequestBody EmployeeUpdateDTO updateDTO) {
        return employeeService.readEmployeeById(id)
                .map(currentEmployee -> {
                    currentEmployee.setName(updateDTO.getName());
                    currentEmployee.setGender(updateDTO.getGender());
                    currentEmployee.setDOB(updateDTO.getDOB());
                    currentEmployee.setContactNo(updateDTO.getContactNo());
                    currentEmployee.setEmail(updateDTO.getEmail());
                    currentEmployee.setPosition(updateDTO.getPosition());
                    currentEmployee.setSalary(updateDTO.getPosition().salary);

                    Employee updatEmployee = employeeService.updateEmployee(currentEmployee);

                    return ResponseEntity.ok(updatEmployee);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        if (employeeService.readEmployeeById(id).isPresent()) {
            employeeService.deleteEmployeeById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
