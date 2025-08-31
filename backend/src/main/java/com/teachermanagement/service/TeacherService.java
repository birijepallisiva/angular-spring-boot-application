package com.teachermanagement.service;

import com.teachermanagement.dto.FilterCriteria;
import com.teachermanagement.dto.TeacherDTO;
import com.teachermanagement.model.Teacher;
import com.teachermanagement.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for Teacher operations
 * Contains business logic for teacher management
 */
@Service
public class TeacherService {
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    /**
     * Get all teachers
     */
    public List<TeacherDTO> getAllTeachers() {
        try {
            List<Teacher> teachers = teacherRepository.findAll();
            return teachers.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving teachers: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get teacher by ID
     */
    public Optional<TeacherDTO> getTeacherById(Long id) {
        Optional<Teacher> teacher = teacherRepository.findById(id);
        return teacher.map(this::convertToDTO);
    }
    
    /**
     * Create a new teacher
     */
    public TeacherDTO createTeacher(TeacherDTO teacherDTO) {
        Teacher teacher = convertToEntity(teacherDTO);
        Teacher savedTeacher = teacherRepository.save(teacher);
        return convertToDTO(savedTeacher);
    }
    
    /**
     * Update an existing teacher
     */
    public Optional<TeacherDTO> updateTeacher(Long id, TeacherDTO teacherDTO) {
        Optional<Teacher> existingTeacher = teacherRepository.findById(id);
        if (existingTeacher.isPresent()) {
            Teacher teacher = existingTeacher.get();
            teacher.setFullName(teacherDTO.getFullName());
            teacher.setDateOfBirth(teacherDTO.getDateOfBirth());
            teacher.setNumberOfClasses(teacherDTO.getNumberOfClasses());
            Teacher updatedTeacher = teacherRepository.save(teacher);
            return Optional.of(convertToDTO(updatedTeacher));
        }
        return Optional.empty();
    }
    
    /**
     * Delete a teacher
     */
    public boolean deleteTeacher(Long id) {
        if (teacherRepository.existsById(id)) {
            teacherRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * Search teachers by name
     */
    public List<TeacherDTO> searchTeachers(String searchTerm) {
        List<Teacher> teachers = teacherRepository.findByFullNameContainingIgnoreCase(searchTerm);
        return teachers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Filter teachers by criteria
     */
    public List<TeacherDTO> filterTeachers(FilterCriteria criteria) {
        LocalDate startDate = null;
        LocalDate endDate = null;
        
        // Convert age range to date range
        if (criteria.getMinAge() != null) {
            endDate = LocalDate.now().minusYears(criteria.getMinAge());
        }
        if (criteria.getMaxAge() != null) {
            startDate = LocalDate.now().minusYears(criteria.getMaxAge() + 1);
        }
        
        List<Teacher> teachers = teacherRepository.findTeachersByCriteria(
                criteria.getSearchTerm(),
                criteria.getMinClasses(),
                criteria.getMaxClasses(),
                startDate,
                endDate
        );
        
        return teachers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get teachers filtered by age range
     */
    public List<TeacherDTO> getTeachersByAgeRange(Integer minAge, Integer maxAge) {
        LocalDate endDate = LocalDate.now().minusYears(minAge);
        LocalDate startDate = LocalDate.now().minusYears(maxAge + 1);
        
        List<Teacher> teachers = teacherRepository.findByDateOfBirthBetween(startDate, endDate);
        return teachers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get teachers filtered by number of classes range
     */
    public List<TeacherDTO> getTeachersByClassesRange(Integer minClasses, Integer maxClasses) {
        List<Teacher> teachers = teacherRepository.findByNumberOfClassesBetween(minClasses, maxClasses);
        return teachers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Calculate average number of classes
     */
    public Double getAverageNumberOfClasses() {
        try {
            Double average = teacherRepository.findAverageNumberOfClasses();
            return average != null ? Math.round(average * 100.0) / 100.0 : 0.0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }
    
    /**
     * Get total number of teachers
     */
    public Long getTotalTeachersCount() {
        return teacherRepository.countTotalTeachers();
    }
    
    /**
     * Convert Teacher entity to TeacherDTO
     */
    private TeacherDTO convertToDTO(Teacher teacher) {
        return new TeacherDTO(
                teacher.getId(),
                teacher.getFullName(),
                teacher.getDateOfBirth(),
                teacher.getNumberOfClasses(),
                teacher.getAge()
        );
    }
    
    /**
     * Convert TeacherDTO to Teacher entity
     */
    private Teacher convertToEntity(TeacherDTO teacherDTO) {
        Teacher teacher = new Teacher();
        teacher.setId(teacherDTO.getId());
        teacher.setFullName(teacherDTO.getFullName());
        teacher.setDateOfBirth(teacherDTO.getDateOfBirth());
        teacher.setNumberOfClasses(teacherDTO.getNumberOfClasses());
        return teacher;
    }
}
