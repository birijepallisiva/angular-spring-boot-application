package com.teachermanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.Period;

/**
 * Teacher entity representing the teacher record in the database
 */
@Entity
@Table(name = "teachers")
public class Teacher {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    @Column(name = "full_name", nullable = false)
    private String fullName;
    
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;
    
    @Min(value = 1, message = "Number of classes must be at least 1")
    @Max(value = 50, message = "Number of classes cannot exceed 50")
    @Column(name = "number_of_classes", nullable = false)
    private Integer numberOfClasses;
    
    // Default constructor
    public Teacher() {}
    
    // Constructor with parameters
    public Teacher(String fullName, LocalDate dateOfBirth, Integer numberOfClasses) {
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.numberOfClasses = numberOfClasses;
    }
    
    // Calculate age from date of birth
    public Integer getAge() {
        if (dateOfBirth == null) {
            return null;
        }
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
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
    
    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", age=" + getAge() +
                ", numberOfClasses=" + numberOfClasses +
                '}';
    }
}
