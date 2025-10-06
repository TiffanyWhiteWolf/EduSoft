package org.example.edusoft.service;

import org.example.edusoft.exception.CourseSectionException;
import org.example.edusoft.model.CourseSection;

import java.util.List;

public interface CourseSectionService {
    
    /**
     * Creates multiple course sections for a given course
     * @param courseId the ID of the course
     * @param sections list of course sections to create
     * @return list of created course sections
     * @throws CourseSectionException if creation fails
     */
    List<CourseSection> createSections(Long courseId, List<CourseSection> sections) throws CourseSectionException;
    
    /**
     * Deletes a specific course section
     * @param sectionId the ID of the section to delete
     * @throws CourseSectionException if deletion fails
     */
    void deleteSection(Long sectionId) throws CourseSectionException;
    
    /**
     * Retrieves all sections for a specific course
     * @param courseId the ID of the course
     * @return list of course sections
     * @throws CourseSectionException if retrieval fails
     */
    List<CourseSection> getSectionsByCourseId(Long courseId) throws CourseSectionException;
}