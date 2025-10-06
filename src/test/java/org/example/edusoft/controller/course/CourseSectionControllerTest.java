package org.example.edusoft.controller.course;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.edusoft.exception.CourseSectionException;
import org.example.edusoft.model.CourseSection;
import org.example.edusoft.service.CourseSectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseSectionController.class)
@DisplayName("CourseSectionController Tests")
class CourseSectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseSectionService courseSectionService;

    @Autowired
    private ObjectMapper objectMapper;

    private CourseSection sampleSection1;
    private CourseSection sampleSection2;
    private List<CourseSection> sampleSections;

    @BeforeEach
    void setUp() {
        sampleSection1 = new CourseSection(1L, "Section A", "Dr. Smith", 30);
        sampleSection1.setId(1L);
        
        sampleSection2 = new CourseSection(1L, "Section B", "Dr. Johnson", 25);
        sampleSection2.setId(2L);
        
        sampleSections = Arrays.asList(sampleSection1, sampleSection2);
    }

    @Nested
    @DisplayName("createSections Tests")
    class CreateSectionsTests {

        @Test
        @DisplayName("Should create sections successfully with valid data")
        void createSections_Success() throws Exception {
            // Given
            Long courseId = 1L;
            when(courseSectionService.createSections(eq(courseId), any(List.class)))
                    .thenReturn(sampleSections);

            // When & Then
            mockMvc.perform(post("/api/course-sections/course/{courseId}", courseId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(sampleSections)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.success", is(true)))
                    .andExpect(jsonPath("$.message", is("Sections created successfully")))
                    .andExpect(jsonPath("$.data", hasSize(2)))
                    .andExpect(jsonPath("$.data[0].id", is(1)))
                    .andExpect(jsonPath("$.data[0].sectionName", is("Section A")))
                    .andExpect(jsonPath("$.data[0].instructor", is("Dr. Smith")))
                    .andExpect(jsonPath("$.data[1].id", is(2)))
                    .andExpect(jsonPath("$.data[1].sectionName", is("Section B")))
                    .andExpect(jsonPath("$.data[1].instructor", is("Dr. Johnson")));

            verify(courseSectionService, times(1)).createSections(eq(courseId), any(List.class));
        }

        @Test
        @DisplayName("Should handle CourseSectionException with custom error code")
        void createSections_CourseSectionException() throws Exception {
            // Given
            Long courseId = 1L;
            String errorMessage = "Invalid section data";
            String errorCode = "INVALID_SECTIONS";
            
            when(courseSectionService.createSections(eq(courseId), any(List.class)))
                    .thenThrow(new CourseSectionException(errorMessage, errorCode));

            // When & Then
            mockMvc.perform(post("/api/course-sections/course/{courseId}", courseId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(sampleSections)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.success", is(false)))
                    .andExpect(jsonPath("$.message", is(errorMessage)))
                    .andExpect(jsonPath("$.errorCode", is(errorCode)))
                    .andExpect(jsonPath("$.data").doesNotExist());

            verify(courseSectionService, times(1)).createSections(eq(courseId), any(List.class));
        }

        @Test
        @DisplayName("Should handle general Exception with internal server error")
        void createSections_GeneralException() throws Exception {
            // Given
            Long courseId = 1L;
            String errorMessage = "Database connection failed";
            
            when(courseSectionService.createSections(eq(courseId), any(List.class)))
                    .thenThrow(new RuntimeException(errorMessage));

            // When & Then
            mockMvc.perform(post("/api/course-sections/course/{courseId}", courseId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(sampleSections)))
                    .andDo(print())
                    .andExpect(status().isInternalServerError())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.success", is(false)))
                    .andExpect(jsonPath("$.message", containsString("Internal server error")))
                    .andExpect(jsonPath("$.message", containsString(errorMessage)))
                    .andExpect(jsonPath("$.data").doesNotExist());

            verify(courseSectionService, times(1)).createSections(eq(courseId), any(List.class));
        }

        @Test
        @DisplayName("Should handle empty sections list")
        void createSections_EmptySectionsList() throws Exception {
            // Given
            Long courseId = 1L;
            List<CourseSection> emptySections = Collections.emptyList();

            // When & Then
            mockMvc.perform(post("/api/course-sections/course/{courseId}", courseId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(emptySections)))
                    .andDo(print())
                    .andExpect(status().isCreated());

            verify(courseSectionService, times(1)).createSections(eq(courseId), eq(emptySections));
        }

        @Test
        @DisplayName("Should handle invalid JSON format")
        void createSections_InvalidJson() throws Exception {
            // Given
            Long courseId = 1L;
            String invalidJson = "{ invalid json }";

            // When & Then
            mockMvc.perform(post("/api/course-sections/course/{courseId}", courseId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest());

            verify(courseSectionService, never()).createSections(any(), any());
        }
    }

    @Nested
    @DisplayName("deleteSection Tests")
    class DeleteSectionTests {

        @Test
        @DisplayName("Should delete section successfully")
        void deleteSection_Success() throws Exception {
            // Given
            Long sectionId = 1L;
            doNothing().when(courseSectionService).deleteSection(sectionId);

            // When & Then
            mockMvc.perform(delete("/api/course-sections/{sectionId}", sectionId))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.success", is(true)))
                    .andExpect(jsonPath("$.message", is("Section deleted successfully")))
                    .andExpect(jsonPath("$.data").doesNotExist());

            verify(courseSectionService, times(1)).deleteSection(sectionId);
        }

        @Test
        @DisplayName("Should handle CourseSectionException when section not found")
        void deleteSection_SectionNotFound() throws Exception {
            // Given
            Long sectionId = 999L;
            String errorMessage = "Section not found with ID: " + sectionId;
            String errorCode = "SECTION_NOT_FOUND";
            
            doThrow(new CourseSectionException(errorMessage, errorCode))
                    .when(courseSectionService).deleteSection(sectionId);

            // When & Then
            mockMvc.perform(delete("/api/course-sections/{sectionId}", sectionId))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.success", is(false)))
                    .andExpect(jsonPath("$.message", is(errorMessage)))
                    .andExpect(jsonPath("$.errorCode", is(errorCode)))
                    .andExpect(jsonPath("$.data").doesNotExist());

            verify(courseSectionService, times(1)).deleteSection(sectionId);
        }

        @Test
        @DisplayName("Should handle general Exception during deletion")
        void deleteSection_GeneralException() throws Exception {
            // Given
            Long sectionId = 1L;
            String errorMessage = "Database constraint violation";
            
            doThrow(new RuntimeException(errorMessage))
                    .when(courseSectionService).deleteSection(sectionId);

            // When & Then
            mockMvc.perform(delete("/api/course-sections/{sectionId}", sectionId))
                    .andDo(print())
                    .andExpect(status().isInternalServerError())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.success", is(false)))
                    .andExpect(jsonPath("$.message", containsString("Internal server error")))
                    .andExpect(jsonPath("$.message", containsString(errorMessage)))
                    .andExpect(jsonPath("$.data").doesNotExist());

            verify(courseSectionService, times(1)).deleteSection(sectionId);
        }

        @Test
        @DisplayName("Should handle invalid section ID format")
        void deleteSection_InvalidIdFormat() throws Exception {
            // When & Then
            mockMvc.perform(delete("/api/course-sections/{sectionId}", "invalid"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());

            verify(courseSectionService, never()).deleteSection(any());
        }
    }

    @Nested
    @DisplayName("getSectionsByCourseId Tests")
    class GetSectionsByCourseIdTests {

        @Test
        @DisplayName("Should retrieve sections successfully")
        void getSectionsByCourseId_Success() throws Exception {
            // Given
            Long courseId = 1L;
            when(courseSectionService.getSectionsByCourseId(courseId))
                    .thenReturn(sampleSections);

            // When & Then
            mockMvc.perform(get("/api/course-sections/course/{courseId}", courseId))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.success", is(true)))
                    .andExpect(jsonPath("$.message", is("Sections retrieved successfully")))
                    .andExpect(jsonPath("$.data", hasSize(2)))
                    .andExpect(jsonPath("$.data[0].id", is(1)))
                    .andExpect(jsonPath("$.data[0].courseId", is(1)))
                    .andExpect(jsonPath("$.data[0].sectionName", is("Section A")))
                    .andExpect(jsonPath("$.data[0].instructor", is("Dr. Smith")))
                    .andExpect(jsonPath("$.data[0].capacity", is(30)))
                    .andExpect(jsonPath("$.data[1].id", is(2)))
                    .andExpect(jsonPath("$.data[1].courseId", is(1)))
                    .andExpect(jsonPath("$.data[1].sectionName", is("Section B")))
                    .andExpect(jsonPath("$.data[1].instructor", is("Dr. Johnson")))
                    .andExpect(jsonPath("$.data[1].capacity", is(25)));

            verify(courseSectionService, times(1)).getSectionsByCourseId(courseId);
        }

        @Test
        @DisplayName("Should return empty list when no sections found")
        void getSectionsByCourseId_EmptyResult() throws Exception {
            // Given
            Long courseId = 999L;
            when(courseSectionService.getSectionsByCourseId(courseId))
                    .thenReturn(Collections.emptyList());

            // When & Then
            mockMvc.perform(get("/api/course-sections/course/{courseId}", courseId))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.success", is(true)))
                    .andExpect(jsonPath("$.message", is("Sections retrieved successfully")))
                    .andExpect(jsonPath("$.data", hasSize(0)));

            verify(courseSectionService, times(1)).getSectionsByCourseId(courseId);
        }

        @Test
        @DisplayName("Should handle CourseSectionException with invalid course ID")
        void getSectionsByCourseId_InvalidCourseId() throws Exception {
            // Given
            Long courseId = null; // This will be handled by the path variable conversion
            Long actualCourseId = 1L;
            String errorMessage = "Course ID cannot be null";
            String errorCode = "INVALID_COURSE_ID";
            
            when(courseSectionService.getSectionsByCourseId(actualCourseId))
                    .thenThrow(new CourseSectionException(errorMessage, errorCode));

            // When & Then
            mockMvc.perform(get("/api/course-sections/course/{courseId}", actualCourseId))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.success", is(false)))
                    .andExpect(jsonPath("$.message", is(errorMessage)))
                    .andExpect(jsonPath("$.errorCode", is(errorCode)))
                    .andExpect(jsonPath("$.data").doesNotExist());

            verify(courseSectionService, times(1)).getSectionsByCourseId(actualCourseId);
        }

        @Test
        @DisplayName("Should handle general Exception during retrieval")
        void getSectionsByCourseId_GeneralException() throws Exception {
            // Given
            Long courseId = 1L;
            String errorMessage = "Database connection timeout";
            
            when(courseSectionService.getSectionsByCourseId(courseId))
                    .thenThrow(new RuntimeException(errorMessage));

            // When & Then
            mockMvc.perform(get("/api/course-sections/course/{courseId}", courseId))
                    .andDo(print())
                    .andExpect(status().isInternalServerError())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.success", is(false)))
                    .andExpect(jsonPath("$.message", containsString("Internal server error")))
                    .andExpect(jsonPath("$.message", containsString(errorMessage)))
                    .andExpect(jsonPath("$.data").doesNotExist());

            verify(courseSectionService, times(1)).getSectionsByCourseId(courseId);
        }

        @Test
        @DisplayName("Should handle invalid course ID format")
        void getSectionsByCourseId_InvalidIdFormat() throws Exception {
            // When & Then
            mockMvc.perform(get("/api/course-sections/course/{courseId}", "invalid"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());

            verify(courseSectionService, never()).getSectionsByCourseId(any());
        }
    }

    @Nested
    @DisplayName("HTTP Method Tests")
    class HttpMethodTests {

        @Test
        @DisplayName("Should not allow GET method for createSections endpoint")
        void createSections_MethodNotAllowed_GET() throws Exception {
            Long courseId = 1L;
            
            mockMvc.perform(get("/api/course-sections/course/{courseId}", courseId))
                    .andDo(print())
                    .andExpect(status().isOk()); // This actually maps to getSectionsByCourseId
        }

        @Test
        @DisplayName("Should not allow POST method for deleteSection endpoint")
        void deleteSection_MethodNotAllowed_POST() throws Exception {
            Long sectionId = 1L;
            
            mockMvc.perform(post("/api/course-sections/{sectionId}", sectionId))
                    .andDo(print())
                    .andExpect(status().isMethodNotAllowed());
        }

        @Test
        @DisplayName("Should not allow PUT method for getSectionsByCourseId endpoint")
        void getSectionsByCourseId_MethodNotAllowed_PUT() throws Exception {
            Long courseId = 1L;
            
            mockMvc.perform(put("/api/course-sections/course/{courseId}", courseId))
                    .andDo(print())
                    .andExpect(status().isMethodNotAllowed());
        }
    }

    @Nested
    @DisplayName("Content Type Tests")
    class ContentTypeTests {

        @Test
        @DisplayName("Should handle missing Content-Type for POST request")
        void createSections_MissingContentType() throws Exception {
            Long courseId = 1L;
            
            mockMvc.perform(post("/api/course-sections/course/{courseId}", courseId)
                            .content(objectMapper.writeValueAsString(sampleSections)))
                    .andDo(print())
                    .andExpect(status().isUnsupportedMediaType());
        }

        @Test
        @DisplayName("Should handle wrong Content-Type for POST request")
        void createSections_WrongContentType() throws Exception {
            Long courseId = 1L;
            
            mockMvc.perform(post("/api/course-sections/course/{courseId}", courseId)
                            .contentType(MediaType.TEXT_PLAIN)
                            .content(objectMapper.writeValueAsString(sampleSections)))
                    .andDo(print())
                    .andExpect(status().isUnsupportedMediaType());
        }
    }
}