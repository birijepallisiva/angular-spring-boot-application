package com.teachermanagement.controller;

import com.teachermanagement.dto.FilterCriteria;
import com.teachermanagement.dto.TeacherDTO;
import com.teachermanagement.service.ExportService;
import com.teachermanagement.service.TeacherService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST Controller for Teacher Management System
 * Handles all HTTP requests related to teacher operations
 */
@RestController
@RequestMapping("/api/teachers")
@CrossOrigin(origins = "*")
public class TeacherController {
    
    @Autowired
    private TeacherService teacherService;
    
    @Autowired
    private ExportService exportService;
    
    /**
     * Get all teachers
     */
    @GetMapping
    public ResponseEntity<List<TeacherDTO>> getAllTeachers() {
        try {
            List<TeacherDTO> teachers = teacherService.getAllTeachers();
            return ResponseEntity.ok(teachers);
        } catch (Exception e) {
            e.printStackTrace(); // Add logging for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Backend is working!");
    }
    
    /**
     * Get teacher by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<TeacherDTO> getTeacherById(@PathVariable Long id) {
        try {
            Optional<TeacherDTO> teacher = teacherService.getTeacherById(id);
            return teacher.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Create a new teacher
     */
    @PostMapping
    public ResponseEntity<TeacherDTO> createTeacher(@Valid @RequestBody TeacherDTO teacherDTO) {
        try {
            TeacherDTO createdTeacher = teacherService.createTeacher(teacherDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTeacher);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    /**
     * Update an existing teacher
     */
    @PutMapping("/{id}")
    public ResponseEntity<TeacherDTO> updateTeacher(@PathVariable Long id, 
                                                   @Valid @RequestBody TeacherDTO teacherDTO) {
        try {
            Optional<TeacherDTO> updatedTeacher = teacherService.updateTeacher(id, teacherDTO);
            return updatedTeacher.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    /**
     * Delete a teacher
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        try {
            boolean deleted = teacherService.deleteTeacher(id);
            return deleted ? ResponseEntity.noContent().build() 
                          : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Search teachers by name
     */
    @GetMapping("/search")
    public ResponseEntity<List<TeacherDTO>> searchTeachers(@RequestParam String query) {
        try {
            List<TeacherDTO> teachers = teacherService.searchTeachers(query);
            return ResponseEntity.ok(teachers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Filter teachers by criteria
     */
    @PostMapping("/filter")
    public ResponseEntity<List<TeacherDTO>> filterTeachers(@RequestBody FilterCriteria criteria) {
        try {
            List<TeacherDTO> teachers = teacherService.filterTeachers(criteria);
            return ResponseEntity.ok(teachers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get teachers by age range
     */
    @GetMapping("/filter/age")
    public ResponseEntity<List<TeacherDTO>> getTeachersByAge(
            @RequestParam Integer minAge, 
            @RequestParam Integer maxAge) {
        try {
            List<TeacherDTO> teachers = teacherService.getTeachersByAgeRange(minAge, maxAge);
            return ResponseEntity.ok(teachers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get teachers by number of classes range
     */
    @GetMapping("/filter/classes")
    public ResponseEntity<List<TeacherDTO>> getTeachersByClasses(
            @RequestParam Integer minClasses, 
            @RequestParam Integer maxClasses) {
        try {
            List<TeacherDTO> teachers = teacherService.getTeachersByClassesRange(minClasses, maxClasses);
            return ResponseEntity.ok(teachers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalTeachers", teacherService.getTotalTeachersCount());
            stats.put("averageClasses", teacherService.getAverageNumberOfClasses());
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Export teachers to PDF
     */
    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportToPDF() {
        try {
            List<TeacherDTO> teachers = teacherService.getAllTeachers();
            byte[] pdfBytes = exportService.exportToPDF(teachers);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "teachers.pdf");
            
            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Export teachers to Excel
     */
    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportToExcel() {
        try {
            List<TeacherDTO> teachers = teacherService.getAllTeachers();
            byte[] excelBytes = exportService.exportToExcel(teachers);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", "teachers.xlsx");
            
            return ResponseEntity.ok().headers(headers).body(excelBytes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
