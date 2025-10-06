package org.example.edusoft.model;

import jakarta.persistence.*;

@Entity
@Table(name = "course_sections")
public class CourseSection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "course_id", nullable = false)
    private Long courseId;
    
    @Column(name = "section_name", nullable = false)
    private String sectionName;
    
    @Column(name = "instructor")
    private String instructor;
    
    @Column(name = "capacity")
    private Integer capacity;
    
    // Default constructor
    public CourseSection() {}
    
    // Constructor
    public CourseSection(Long courseId, String sectionName, String instructor, Integer capacity) {
        this.courseId = courseId;
        this.sectionName = sectionName;
        this.instructor = instructor;
        this.capacity = capacity;
    }
    
    // Getters and setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getCourseId() {
        return courseId;
    }
    
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
    
    public String getSectionName() {
        return sectionName;
    }
    
    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }
    
    public String getInstructor() {
        return instructor;
    }
    
    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }
    
    public Integer getCapacity() {
        return capacity;
    }
    
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}