package com.example.userpostapi.service;

import com.example.userpostapi.exception.DataAccessException;
import com.example.userpostapi.exception.OperationFailedException;
import com.example.userpostapi.exception.ResourceNotFoundException;
import com.example.userpostapi.model.Post;
import com.example.userpostapi.repository.PostRepository;
import com.example.userpostapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Post getPostById(Long id) {
        try {
            return postRepository.findById(id)
                    .orElseThrow(() -> new DataAccessException("Post with id " + id + " not found."));
        } catch (DataAccessException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post createPost(Post post) {
        if (!userRepository.existsById(post.getUserId())) {
            throw new OperationFailedException("Cannot create post. User with id " + post.getUserId() + " does not exist.");
        }
        return postRepository.save(post);
    }

    @Override
    public Post updatePost(Long id, Post postUpdates) {
        try {
            Post existingPost = postRepository.findById(id)
                    .orElseThrow(() -> new DataAccessException("Post with id " + id + " not found."));

            if (!userRepository.existsById(postUpdates.getUserId())) {
                throw new OperationFailedException("Cannot update post. User with id " + postUpdates.getUserId() + " does not exist.");
            }

            existingPost.setTitle(postUpdates.getTitle());
            existingPost.setContent(postUpdates.getContent());
            existingPost.setUserId(postUpdates.getUserId());

            return postRepository.save(existingPost);
        } catch (DataAccessException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public void deletePost(Long id) {
        try {
            postRepository.deleteById(id);
        } catch (DataAccessException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
}
