package com.teachermanagement.dto;

/**
 * DTO for filter criteria used in teacher search and filtering
 */
public class FilterCriteria {
    
    private Integer minAge;
    private Integer maxAge;
    private Integer minClasses;
    private Integer maxClasses;
    private String searchTerm;
    
    // Default constructor
    public FilterCriteria() {}
    
    // Constructor with parameters
    public FilterCriteria(Integer minAge, Integer maxAge, Integer minClasses, Integer maxClasses, String searchTerm) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.minClasses = minClasses;
        this.maxClasses = maxClasses;
        this.searchTerm = searchTerm;
    }
    
    // Getters and Setters
    public Integer getMinAge() {
        return minAge;
    }
    
    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }
    
    public Integer getMaxAge() {
        return maxAge;
    }
    
    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }
    
    public Integer getMinClasses() {
        return minClasses;
    }
    
    public void setMinClasses(Integer minClasses) {
        this.minClasses = minClasses;
    }
    
    public Integer getMaxClasses() {
        return maxClasses;
    }
    
    public void setMaxClasses(Integer maxClasses) {
        this.maxClasses = maxClasses;
    }
    
    public String getSearchTerm() {
        return searchTerm;
    }
    
    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }
}
