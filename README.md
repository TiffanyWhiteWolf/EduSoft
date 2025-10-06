# EduSoft - Educational Software System

## Overview
This is a Spring Boot application that manages course sections. The system provides REST API endpoints for creating, deleting, and retrieving course sections.

## Project Structure
```
src/
├── main/java/org/example/edusoft/
│   ├── EduSoftApplication.java          # Main Spring Boot application
│   ├── controller/course/
│   │   └── CourseSectionController.java # REST controller for course sections
│   ├── service/
│   │   ├── CourseSectionService.java    # Service interface
│   │   └── CourseSectionServiceImpl.java # Service implementation
│   ├── model/
│   │   ├── CourseSection.java           # Course section entity
│   │   └── Result.java                  # Generic result wrapper
│   └── exception/
│       └── CourseSectionException.java  # Custom exception class
└── test/java/org/example/edusoft/controller/course/
    └── CourseSectionControllerTest.java # Comprehensive unit tests
```

## API Endpoints

### 1. Create Sections
- **POST** `/api/course-sections/course/{courseId}`
- Creates multiple course sections for a given course
- Request body: List of CourseSection objects
- Response: Result with created sections

### 2. Delete Section
- **DELETE** `/api/course-sections/{sectionId}`
- Deletes a specific course section
- Response: Result indicating success or failure

### 3. Get Sections by Course ID
- **GET** `/api/course-sections/course/{courseId}`
- Retrieves all sections for a specific course
- Response: Result with list of sections

## Testing

The project includes comprehensive unit tests for the `CourseSectionController` with:

- **19 total test cases** covering all controller methods
- **Positive and negative scenarios** for each endpoint
- **Exception handling** tests for both `CourseSectionException` and general exceptions
- **HTTP method validation** tests
- **Content type validation** tests
- **JSON request/response validation**

### Test Categories

1. **CreateSectionsTests** (5 tests)
   - Success scenarios
   - CourseSectionException handling
   - General exception handling
   - Empty sections list handling
   - Invalid JSON format handling

2. **DeleteSectionTests** (4 tests)
   - Success scenarios
   - Section not found handling
   - General exception handling
   - Invalid ID format handling

3. **GetSectionsByCourseIdTests** (5 tests)
   - Success scenarios
   - Empty result handling
   - Invalid course ID handling
   - General exception handling
   - Invalid ID format handling

4. **HttpMethodTests** (3 tests)
   - Method not allowed scenarios

5. **ContentTypeTests** (2 tests)
   - Missing/wrong content type handling

## Building and Running

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Build the project
```bash
mvn clean compile
```

### Run tests
```bash
mvn test
```

### Package the application
```bash
mvn package
```

### Run the application
```bash
java -jar target/edusoft-1.0.0.jar
```

## Technology Stack
- **Spring Boot 3.1.0**
- **Spring Web MVC**
- **Spring Data JPA**
- **H2 Database** (in-memory for testing)
- **JUnit 5** for testing
- **Mockito** for mocking
- **MockMvc** for web layer testing

## Features
- RESTful API design
- Comprehensive error handling
- Custom exception types with error codes
- Generic Result wrapper for consistent responses
- Full test coverage with positive and negative scenarios
- Spring Boot best practices implementation