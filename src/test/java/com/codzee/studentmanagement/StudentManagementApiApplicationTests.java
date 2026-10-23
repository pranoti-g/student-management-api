
package com.codzee.studentmanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StudentManagementApiApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
    }

    @Test
    void shouldNotExposePhoneNumbersInStudentResponse() throws Exception {
        studentRepository.save(
                new Student("Pranoti", 27, "A", "9876543210"));

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Pranoti"))
                .andExpect(jsonPath("$.content[0].phone").doesNotExist());
    }

    @Test
    void shouldRejectPageWhoseOffsetOverflowsInteger() throws Exception {
        mockMvc.perform(get("/api/students")
                        .param("page", "21474836")
                        .param("size", "100"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldSupportSearchCombinedWithPagination() throws Exception {
        studentRepository.save(
                new Student("Pranoti", 27, "A", "9876543210"));
        studentRepository.save(
                new Student("Prakash", 25, "B", "9876543211"));
        studentRepository.save(
                new Student("Swaraj", 28, "A", "9876543212"));

        mockMvc.perform(get("/api/students")
                        .param("name", "pra")
                        .param("page", "0")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.totalElements").value(2));
    }
}
