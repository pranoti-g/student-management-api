
package com.codzee.studentmanagement;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Pattern(regexp = "[\\p{L}\\p{M} ]+",
             message = "Name must contain letters and spaces only")
    private String name;

    @NotNull
    @Min(1)
    @Max(100)
    private Integer age;

    @NotBlank
    @Size(max = 50)
    private String className;

    @NotBlank
    @Pattern(regexp = "\\d{10}",
             message = "Phone must contain exactly 10 digits")
    private String phone;

    protected Student() {
    }

    public Student(String name, Integer age, String className, String phone) {
        this.name = name;
        this.age = age;
        this.className = className;
        this.phone = phone;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
