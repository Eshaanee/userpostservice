package com.example.userpostapi.controller;



import com.example.userpostapi.model.Post;
import com.example.userpostapi.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable Long id) { //
        return postService.getPostById(id);
    }

    @GetMapping
    public List<Post> getAllPosts() { //
        return postService.getAllPosts();
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@Valid @RequestBody Post post) { //
        Post createdPost = postService.createPost(post);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public Post updatePost(@PathVariable Long id, @Valid @RequestBody Post post) { //
        return postService.updatePost(id, post);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) { //
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
