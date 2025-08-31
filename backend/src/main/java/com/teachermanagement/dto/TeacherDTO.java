package com.teachermanagement.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

/**
 * Data Transfer Object for Teacher
 * Used for API requests and responses
 */
public class TeacherDTO {
    
    private Long id;
    
    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;
    
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;
    
    @Min(value = 1, message = "Number of classes must be at least 1")
    @Max(value = 50, message = "Number of classes cannot exceed 50")
    private Integer numberOfClasses;
    
    private Integer age;
    
    // Default constructor
    public TeacherDTO() {}
    
    // Constructor with parameters
    public TeacherDTO(Long id, String fullName, LocalDate dateOfBirth, Integer numberOfClasses, Integer age) {
        this.id = id;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.numberOfClasses = numberOfClasses;
        this.age = age;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public Integer getNumberOfClasses() {
        return numberOfClasses;
    }
    
    public void setNumberOfClasses(Integer numberOfClasses) {
        this.numberOfClasses = numberOfClasses;
    }
    
    public Integer getAge() {
        return age;
    }
    
    public void setAge(Integer age) {
        this.age = age;
    }
}
