package com.example.demowithtests.util.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final static String EMPLOYEE_NOT_FOUND = "Employee not found with id = ";
    private final static String EMPLOYEE_WAS_DELETED = "Employee was deleted with id = ";
    private final static String EMPLOYEE_CONTAINS = "Employee already exists!";

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(WebRequest request) {
        ErrorDetails errorDetails =
                new ErrorDetails(new Date(),
                        EMPLOYEE_NOT_FOUND + getIdFromWebRequest(request),
                        request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceWasDeletedException.class)
    protected ResponseEntity<?> resourceWasDeletedException(WebRequest request) {
        ErrorDetails errorDetails =
                new ErrorDetails(new Date(),
                        EMPLOYEE_WAS_DELETED + getIdFromWebRequest(request),
                        request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmployeeContainsException.class)
    protected ResponseEntity<?> employeeContainsException(WebRequest request) {
        ErrorDetails errorDetails =
                new ErrorDetails(new Date(),
                        EMPLOYEE_CONTAINS,
                        request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest request) {
        ErrorDetails errorDetails =
                new ErrorDetails(new Date(),
                        ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static String getIdFromWebRequest(WebRequest request) {
        String path = request.getDescription(false);
        String[] pathSegments = path.split("/");
        return pathSegments[pathSegments.length - 1];
    }
}
