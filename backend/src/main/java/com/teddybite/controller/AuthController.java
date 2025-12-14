package com.teddybite.controller;

import com.teddybite.dto.LoginDTO;
import com.teddybite.entity.Employee;
import com.teddybite.repository.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        Optional<Employee> employeeOpt = employeeRepository.findByEmail(loginDTO.getEmail());

        if (employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();
            if (passwordEncoder.matches(loginDTO.getPassword(), employee.getPassword())) {
                // Return employee details (password is JsonIgnored)
                return ResponseEntity.ok(employee);
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
    }
}
