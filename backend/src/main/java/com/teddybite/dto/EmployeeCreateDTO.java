package com.teddybite.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

import com.teddybite.entity.types.EmployeePosition;
import com.teddybite.entity.types.Gender;

@Data
public class EmployeeCreateDTO {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Name must contain only letters and spaces")
    private String name;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotNull(message = "Date of Birth is required")
    @Past(message = "Date of Birth must be in the past")
    private LocalDateTime dob;

    @NotBlank(message = "Contact Number is required")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Contact Number must be a valid phone number (10-15 digits)")
    private String contactNo;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Position is required")
    private EmployeePosition position;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!.])(?=\\S+$).{8,}$", message = "Password must contain at least 8 chars, 1 uppercase, 1 lowercase, 1 digit, and 1 special char (e.g. @#$%^&+=!.)")
    private String password;
}
