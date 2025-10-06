package org.example.edusoft.service;

import org.example.edusoft.exception.CourseSectionException;
import org.example.edusoft.model.CourseSection;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CourseSectionServiceImpl implements CourseSectionService {
    
    // In-memory storage for demonstration purposes
    private final Map<Long, CourseSection> sectionsStore = new HashMap<>();
    private Long nextId = 1L;
    
    @Override
    public List<CourseSection> createSections(Long courseId, List<CourseSection> sections) throws CourseSectionException {
        if (courseId == null) {
            throw new CourseSectionException("Course ID cannot be null", "INVALID_COURSE_ID");
        }
        
        if (sections == null || sections.isEmpty()) {
            throw new CourseSectionException("Sections list cannot be null or empty", "INVALID_SECTIONS");
        }
        
        List<CourseSection> createdSections = new ArrayList<>();
        
        for (CourseSection section : sections) {
            if (section.getSectionName() == null || section.getSectionName().trim().isEmpty()) {
                throw new CourseSectionException("Section name cannot be null or empty", "INVALID_SECTION_NAME");
            }
            
            section.setId(nextId++);
            section.setCourseId(courseId);
            sectionsStore.put(section.getId(), section);
            createdSections.add(section);
        }
        
        return createdSections;
    }
    
    @Override
    public void deleteSection(Long sectionId) throws CourseSectionException {
        if (sectionId == null) {
            throw new CourseSectionException("Section ID cannot be null", "INVALID_SECTION_ID");
        }
        
        if (!sectionsStore.containsKey(sectionId)) {
            throw new CourseSectionException("Section not found with ID: " + sectionId, "SECTION_NOT_FOUND");
        }
        
        sectionsStore.remove(sectionId);
    }
    
    @Override
    public List<CourseSection> getSectionsByCourseId(Long courseId) throws CourseSectionException {
        if (courseId == null) {
            throw new CourseSectionException("Course ID cannot be null", "INVALID_COURSE_ID");
        }
        
        return sectionsStore.values().stream()
                .filter(section -> courseId.equals(section.getCourseId()))
                .collect(Collectors.toList());
    }
}