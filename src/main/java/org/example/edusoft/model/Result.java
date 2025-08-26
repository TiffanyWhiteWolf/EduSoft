package org.example.edusoft.model;

public class Result<T> {
    private boolean success;
    private String message;
    private T data;
    private String errorCode;
    
    // Default constructor
    public Result() {}
    
    // Success constructor
    public Result(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
    
    // Error constructor
    public Result(boolean success, String message, String errorCode) {
        this.success = success;
        this.message = message;
        this.errorCode = errorCode;
    }
    
    // Static factory methods
    public static <T> Result<T> success(T data) {
        return new Result<>(true, "Operation successful", data);
    }
    
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(true, message, data);
    }
    
    public static <T> Result<T> error(String message) {
        return new Result<>(false, message, (T) null);
    }
    
    public static <T> Result<T> error(String message, String errorCode) {
        return new Result<>(false, message, errorCode);
    }
    
    // Getters and setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}