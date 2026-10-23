
package com.codzee.studentmanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
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

        studentRepository.save(
                new Student("Pranoti", 27, "A", "9876543210"));
        studentRepository.save(
                new Student("Prakash", 25, "B", "9876543211"));
        studentRepository.save(
                new Student("Swaraj", 28, "A", "9876543212"));
    }

    @Test
    void shouldReturnFirstPageWithRequestedSize() throws Exception {
        mockMvc.perform(get("/api/students")
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.totalElements", is(3)))
                .andExpect(jsonPath("$.totalPages", is(2)));
    }

    @Test
    void shouldReturnSecondPage() throws Exception {
        mockMvc.perform(get("/api/students")
                        .param("page", "1")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.totalElements", is(3)));
    }

    @Test
    void shouldSearchNamesWithoutCaseSensitivity() throws Exception {
        mockMvc.perform(get("/api/students")
                        .param("name", "PRA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.totalElements", is(2)));
    }

    @Test
    void shouldReturnBadRequestForNegativePage() throws Exception {
        mockMvc.perform(get("/api/students")
                        .param("page", "-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenPageSizeExceedsLimit() throws Exception {
        mockMvc.perform(get("/api/students")
                        .param("size", "101"))
                .andExpect(status().isBadRequest());
    }
}
