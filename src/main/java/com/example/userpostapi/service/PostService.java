package com.example.userpostapi.service;


import com.example.userpostapi.model.Post;
import java.util.List;

public interface PostService {
    Post getPostById(Long id);
    List<Post> getAllPosts();
    Post createPost(Post post);
    Post updatePost(Long id, Post post);
    void deletePost(Long id);
}