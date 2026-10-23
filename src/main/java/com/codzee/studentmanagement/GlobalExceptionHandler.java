
package com.codzee.studentmanagement;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException exception) {

        Map<String, String> errors = new LinkedHashMap<>();

        exception.getBindingResult().getFieldErrors().forEach(error ->
                errors.putIfAbsent(
                        error.getField(),
                        error.getDefaultMessage()
                )
        );

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 400);
        response.put("error", "Validation failed");
        response.put("fieldErrors", errors);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
            StudentNotFoundException exception) {

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 404);
        response.put("error", exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidBody(
            HttpMessageNotReadableException exception) {

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 400);
        response.put("error", "Request body is invalid or malformed");

        return ResponseEntity.badRequest().body(response);
    }
}
