package org.example.edusoft.controller.course;

import org.example.edusoft.exception.CourseSectionException;
import org.example.edusoft.model.CourseSection;
import org.example.edusoft.model.Result;
import org.example.edusoft.service.CourseSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course-sections")
public class CourseSectionController {
    
    @Autowired
    private CourseSectionService courseSectionService;
    
    /**
     * Creates multiple course sections for a given course
     * @param courseId the ID of the course
     * @param sections list of course sections to create
     * @return ResponseEntity with Result containing created sections
     */
    @PostMapping("/course/{courseId}")
    public ResponseEntity<Result<List<CourseSection>>> createSections(
            @PathVariable Long courseId,
            @RequestBody List<CourseSection> sections) {
        try {
            List<CourseSection> createdSections = courseSectionService.createSections(courseId, sections);
            Result<List<CourseSection>> result = Result.success("Sections created successfully", createdSections);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (CourseSectionException e) {
            Result<List<CourseSection>> result = Result.error(e.getMessage(), e.getErrorCode());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        } catch (Exception e) {
            Result<List<CourseSection>> result = Result.error("Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
    
    /**
     * Deletes a specific course section
     * @param sectionId the ID of the section to delete
     * @return ResponseEntity with Result indicating success or failure
     */
    @DeleteMapping("/{sectionId}")
    public ResponseEntity<Result<Void>> deleteSection(@PathVariable Long sectionId) {
        try {
            courseSectionService.deleteSection(sectionId);
            Result<Void> result = Result.success("Section deleted successfully", null);
            return ResponseEntity.ok(result);
        } catch (CourseSectionException e) {
            Result<Void> result = Result.error(e.getMessage(), e.getErrorCode());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        } catch (Exception e) {
            Result<Void> result = Result.error("Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
    
    /**
     * Retrieves all sections for a specific course
     * @param courseId the ID of the course
     * @return ResponseEntity with Result containing list of sections
     */
    @GetMapping("/course/{courseId}")
    public ResponseEntity<Result<List<CourseSection>>> getSectionsByCourseId(@PathVariable Long courseId) {
        try {
            List<CourseSection> sections = courseSectionService.getSectionsByCourseId(courseId);
            Result<List<CourseSection>> result = Result.success("Sections retrieved successfully", sections);
            return ResponseEntity.ok(result);
        } catch (CourseSectionException e) {
            Result<List<CourseSection>> result = Result.error(e.getMessage(), e.getErrorCode());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        } catch (Exception e) {
            Result<List<CourseSection>> result = Result.error("Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
}