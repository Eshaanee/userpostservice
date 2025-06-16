package com.example.userpostapi.repository;

import com.example.userpostapi.exception.DataAccessException;
import com.example.userpostapi.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private final Map<Long, User> userStore = new ConcurrentHashMap<>();
    private final AtomicLong userIdCounter = new AtomicLong();

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(userStore.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userStore.values());
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(userIdCounter.incrementAndGet());
        }
        userStore.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteById(Long id) {
        if (!userStore.containsKey(id)) {
            throw new DataAccessException("Cannot delete. User with id " + id + " does not exist.");
        }
        userStore.remove(id);
    }

    @Override
    public boolean existsById(Long id) {
        return userStore.containsKey(id);
    }
}
