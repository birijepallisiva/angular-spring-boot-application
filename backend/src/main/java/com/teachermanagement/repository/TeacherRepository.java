package com.teachermanagement.repository;

import com.teachermanagement.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for Teacher entity
 * Provides database operations for teachers
 */
@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    
    /**
     * Find teachers by full name containing the search term (case-insensitive)
     */
    List<Teacher> findByFullNameContainingIgnoreCase(String fullName);
    
    /**
     * Find teachers by number of classes
     */
    List<Teacher> findByNumberOfClasses(Integer numberOfClasses);
    
    /**
     * Find teachers by number of classes between min and max values
     */
    List<Teacher> findByNumberOfClassesBetween(Integer minClasses, Integer maxClasses);
    
    /**
     * Find teachers by date of birth between two dates (for age filtering)
     */
    List<Teacher> findByDateOfBirthBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Custom query to filter teachers by multiple criteria
     */
    @Query("SELECT t FROM Teacher t WHERE " +
           "(:searchTerm IS NULL OR LOWER(t.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "(:minClasses IS NULL OR t.numberOfClasses >= :minClasses) AND " +
           "(:maxClasses IS NULL OR t.numberOfClasses <= :maxClasses) AND " +
           "(:startDate IS NULL OR t.dateOfBirth >= :startDate) AND " +
           "(:endDate IS NULL OR t.dateOfBirth <= :endDate)")
    List<Teacher> findTeachersByCriteria(
            @Param("searchTerm") String searchTerm,
            @Param("minClasses") Integer minClasses,
            @Param("maxClasses") Integer maxClasses,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
    
    /**
     * Calculate average number of classes
     */
    @Query("SELECT AVG(t.numberOfClasses) FROM Teacher t")
    Double findAverageNumberOfClasses();
    
    /**
     * Count total number of teachers
     */
    @Query("SELECT COUNT(t) FROM Teacher t")
    Long countTotalTeachers();
}
