package com.example.userpostapi.repository;


import com.example.userpostapi.exception.DataAccessException;
import com.example.userpostapi.model.Post;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryPostRepository implements PostRepository {

    private final Map<Long, Post> postStore = new ConcurrentHashMap<>();
    private final AtomicLong postIdCounter = new AtomicLong();

    @Override
    public Optional<Post> findById(Long id) {
        return Optional.ofNullable(postStore.get(id));
    }

    @Override
    public List<Post> findAll() {
        return new ArrayList<>(postStore.values());
    }

    @Override
    public Post save(Post post) {
        if (post.getId() == null) {
            post.setId(postIdCounter.incrementAndGet());
        }
        postStore.put(post.getId(), post);
        return post;
    }

    @Override
    public void deleteById(Long id) {
        if (!postStore.containsKey(id)) {
            throw new DataAccessException("Cannot delete. Post with id " + id + " does not exist.");
        }
        postStore.remove(id);
    }
}