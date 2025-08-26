package org.example.edusoft.exception;

public class CourseSectionException extends Exception {
    private String errorCode;
    
    public CourseSectionException(String message) {
        super(message);
    }
    
    public CourseSectionException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public CourseSectionException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public CourseSectionException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}