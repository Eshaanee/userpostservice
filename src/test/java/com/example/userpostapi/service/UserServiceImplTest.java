package com.example.userpostapi.service;

import static org.junit.jupiter.api.Assertions.*;


import com.example.userpostapi.exception.DataAccessException;
import com.example.userpostapi.exception.ResourceNotFoundException;
import com.example.userpostapi.model.User;
import com.example.userpostapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
    }

    // --- Test for Get All Users (Non-Paginated) ---
    @Test
    @DisplayName("Get all users should return a list of users")
    void whenGetAllUsers_shouldReturnListOfUsers() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Test User");
    }

    // --- Other unit tests (get by ID, create, update, delete) remain the same ---

    @Test
    @DisplayName("Get user by ID should return user when found")
    void whenGetUserById_withValidId_shouldReturnUser() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        User foundUser = userService.getUserById(1L);

        // Assert
        assertNotNull(foundUser);
        assertEquals("Test User", foundUser.getName());
    }

    @Test
    @DisplayName("Get user by ID should throw exception when not found")
    void whenGetUserById_withInvalidId_shouldThrowResourceNotFoundException() {
        // Arrange
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(99L);
        });
    }
}