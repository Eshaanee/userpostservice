package com.example.userpostapi.service;

import com.example.userpostapi.exception.*;
import com.example.userpostapi.model.User;
import com.example.userpostapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserById(Long id) {
        try {
            return userRepository.findById(id)
                    .orElseThrow(() -> new DataAccessException("User with id " + id + " not found."));
        } catch (DataAccessException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Retryable(value = {TransientDataAccessException.class}, maxAttempts = 3, backoff = @Backoff(delay = 100))
    public User createUser(User user) {
        log.info("Attempting to save user...");
        return userRepository.save(user);
    }

    @Recover
    public User recoverFromCreateUser(TransientDataAccessException e, User user) {
        log.error("All retries failed for creating user '{}'. Reason: {}", user.getName(), e.getMessage());
        throw new OperationFailedException("Service is currently unable to create user. Please try again later.");
    }

    @Override
    public User updateUser(Long id, User userUpdates) {
        try {
            User existingUser = userRepository.findById(id)
                    .orElseThrow(() -> new DataAccessException("User with id " + id + " not found."));
            existingUser.setName(userUpdates.getName());
            existingUser.setEmail(userUpdates.getEmail());
            return userRepository.save(existingUser);
        } catch (DataAccessException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public void deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (DataAccessException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
}
