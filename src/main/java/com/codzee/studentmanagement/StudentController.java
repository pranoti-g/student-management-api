
package com.codzee.studentmanagement;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(
            @Valid @RequestBody Student student) {

        // Prevent clients from supplying their own database ID.
        student.setId(null);

        Student savedStudent = studentRepository.save(student);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedStudent);
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    @PutMapping("/{id}")
    public Student updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody Student studentDetails) {

        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));

        existingStudent.setName(studentDetails.getName());
        existingStudent.setAge(studentDetails.getAge());
        existingStudent.setClassName(studentDetails.getClassName());
        existingStudent.setPhone(studentDetails.getPhone());

        return studentRepository.save(existingStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));

        studentRepository.delete(student);

        return ResponseEntity.noContent().build();
    }
}
