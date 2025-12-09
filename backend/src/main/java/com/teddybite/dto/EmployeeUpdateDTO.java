package com.teddybite.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

import com.teddybite.entity.EmployeePosition;
import com.teddybite.entity.Gender;

@Data
public class EmployeeUpdateDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotNull(message = "Date of Birth is required")
    @Past(message = "Date of Birth must be in the past")
    private LocalDateTime dob;

    @NotBlank(message = "Contact Number is required")
    private String contactNo;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Position is required")
    private EmployeePosition position;
}
