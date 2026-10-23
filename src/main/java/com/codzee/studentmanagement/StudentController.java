
package com.codzee.studentmanagement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private static final int MAX_PAGE_SIZE = 100;

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping
    public ResponseEntity<?> getStudents(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (page < 0 || page > 1000000 || size < 1 || size > MAX_PAGE_SIZE) {
        return ResponseEntity.badRequest()
                    .body("Page must be between 0 and 1000000, and size must be between 1 and 100");
        }

        long offset = (long) page * size;

        if (offset > Integer.MAX_VALUE) {
            return ResponseEntity.badRequest()
                    .body("Requested page is too large");
        }

        Pageable pageable = PageRequest.of(
                page, size, Sort.by(Sort.Direction.ASC, "id"));

        Page<Student> students;

        if (name != null && !name.isBlank()) {
            students = studentRepository.findByNameContainingIgnoreCase(
                    name.trim(), pageable);
        } else {
            students = studentRepository.findAll(pageable);
        }

        Page<StudentResponse> response = students.map(StudentResponse::from);
        return ResponseEntity.ok(response);
    }
}
