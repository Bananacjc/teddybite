package com.teddybite.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testHandleDuplicateResourceException() throws Exception {
        mockMvc.perform(post("/test/validation")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name", is("Name is required")));
    }

    @Test
    void testHandleValidationExceptions() throws Exception {
        mockMvc.perform(get("/test/duplicate"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email", is("This email is taken")));
    }

    @RestController
    static class TestController {

        @PostMapping("/test/validation")
        void testValidation(@Valid @RequestBody TestDTO dto) {

        }

        @GetMapping("/test/duplicate")
        void testDuplicate() {
            throw new DuplicateResourceException("email", "This email is taken");
        }
    }

    static class TestDTO {
        @NotNull(message = "Name is required")
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
