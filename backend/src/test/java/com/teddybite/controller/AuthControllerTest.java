package com.teddybite.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teddybite.dto.LoginDTO;
import com.teddybite.entity.Employee;
import com.teddybite.entity.types.EmployeePosition;
import com.teddybite.entity.types.Gender;
import com.teddybite.repository.EmployeeRepository;
import com.teddybite.repository.OrderRepository; // <--- Import this
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
    controllers = AuthController.class,
    excludeAutoConfiguration = {
        SecurityAutoConfiguration.class,
        OAuth2ClientAutoConfiguration.class,
        OAuth2ResourceServerAutoConfiguration.class
    }
)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private OrderRepository orderRepository; 

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee sampleEmployee;
    private LoginDTO loginDTO;

    @BeforeEach
    void setUp() {
        sampleEmployee = new Employee();
        sampleEmployee.setEmployeeID("EMP001");
        sampleEmployee.setName("Admin User");
        sampleEmployee.setEmail("admin@teddybite.com");
        sampleEmployee.setPassword("encoded_secret_password");
        sampleEmployee.setPosition(EmployeePosition.MANAGER);
        sampleEmployee.setGender(Gender.MALE);

        loginDTO = new LoginDTO();
        loginDTO.setEmail("admin@teddybite.com");
        loginDTO.setPassword("RawPassword123");
    }

    @Test
    void testLogin_Success() throws Exception {
        when(employeeRepository.findByEmail(loginDTO.getEmail()))
                .thenReturn(Optional.of(sampleEmployee));

        when(passwordEncoder.matches(loginDTO.getPassword(), sampleEmployee.getPassword()))
                .thenReturn(true);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("admin@teddybite.com")))
                .andExpect(jsonPath("$.name", is("Admin User")));
    }

    @Test
    void testLogin_InvalidEmail_ReturnsUnauthorized() throws Exception {
        when(employeeRepository.findByEmail("unknown@teddybite.com"))
                .thenReturn(Optional.empty());

        LoginDTO invalidEmailDTO = new LoginDTO();
        invalidEmailDTO.setEmail("unknown@teddybite.com");
        invalidEmailDTO.setPassword("password");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidEmailDTO)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid email or password"));
    }

    @Test
    void testLogin_InvalidPassword_ReturnsUnauthorized() throws Exception {
        when(employeeRepository.findByEmail(loginDTO.getEmail()))
                .thenReturn(Optional.of(sampleEmployee));

        when(passwordEncoder.matches("WrongPassword", sampleEmployee.getPassword()))
                .thenReturn(false);

        LoginDTO wrongPassDTO = new LoginDTO();
        wrongPassDTO.setEmail("admin@teddybite.com");
        wrongPassDTO.setPassword("WrongPassword");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wrongPassDTO)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid email or password"));
    }
}