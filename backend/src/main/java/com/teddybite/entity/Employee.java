package com.teddybite.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

/**
 * The Employee class
 */
@Data
@Document(collection = "employees")
public class Employee{
    @Id
    private String employeeID;
    @JsonIgnore
    private String password;
    private String name;
    private Gender gender;
    private LocalDateTime DOB;
    private String contactNo;
    private String email;
    private LocalDateTime dateJoined;
    private EmployeePosition position;
    private double salary;
}
