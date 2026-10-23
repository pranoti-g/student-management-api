
package com.codzee.studentmanagement;

public record StudentResponse(
        Long id,
        String name,
        Integer age,
        String className) {

    public static StudentResponse from(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getName(),
                student.getAge(),
                student.getClassName());
    }
}
