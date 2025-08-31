package com.teachermanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for Teacher Management System
 * This is the entry point of the Spring Boot application
 */
@SpringBootApplication
public class TeacherManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeacherManagementApplication.class, args);
        System.out.println("Teacher Management System Backend started successfully!");
    }
}
