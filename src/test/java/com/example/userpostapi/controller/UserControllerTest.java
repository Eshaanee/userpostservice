package com.example.userpostapi.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.example.userpostapi.model.User;
import com.example.userpostapi.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @WebMvcTest remains the same. It sets up the web layer for testing.
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Instead of @MockBean, we now @Autowired the mock.
    // Spring will find it in our static TestConfig class below.
    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * This static nested class provides the mock bean definition.
     * Spring will detect this configuration and use it for this test class only.
     */
    @TestConfiguration
    static class UserControllerTestConfig {
        @Bean
        public UserService userService() {
            // Create and return a mock of the UserService.
            return Mockito.mock(UserService.class);
        }
    }

    @Test
    void whenPostUser_shouldReturnCreatedStatusAndUser() throws Exception {
        // Arrange
        User userToCreate = new User();
        userToCreate.setName("Test User");
        userToCreate.setEmail("test@example.com");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("Test User");
        savedUser.setEmail("test@example.com");

        // Use the autowired mock service
        when(userService.createUser(any(User.class))).thenReturn(savedUser);

        // Act & Assert
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    void whenGetUserWithValidId_shouldReturnUser() throws Exception {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setName("Test User");

        // Use the autowired mock service
        when(userService.getUserById(1L)).thenReturn(user);

        // Act & Assert
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test User"));
    }
}