package com.example.userpostapi.repository;


import com.example.userpostapi.model.Post;
import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Optional<Post> findById(Long id);
    List<Post> findAll();
    Post save(Post post);
    void deleteById(Long id);
}