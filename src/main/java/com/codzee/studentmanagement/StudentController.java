
package com.codzee.studentmanagement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping
    public ResponseEntity<?> getStudents(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (page < 0 || size < 1 || size > 100) {
            return ResponseEntity.badRequest()
                    .body("Page must be >= 0 and size must be between 1 and 100");
        }

        Pageable pageable = PageRequest.of(
                page, size, Sort.by(Sort.Direction.ASC, "id"));

        Page<Student> results;

        if (name != null && !name.isBlank()) {
            results = studentRepository.findByNameContainingIgnoreCase(
                    name.trim(), pageable);
        } else {
            results = studentRepository.findAll(pageable);
        }

        return ResponseEntity.ok(results);
    }
}
