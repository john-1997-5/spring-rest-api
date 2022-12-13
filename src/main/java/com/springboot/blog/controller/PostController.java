package com.springboot.blog.controller;

import com.springboot.blog.entity.Post;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@Slf4j
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto) {
        PostDto postDtoResponse = postService.createPost(postDto);
        return new ResponseEntity<>(postDtoResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PostResponse> getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PostResponse postResponse = postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long id, @RequestBody PostDto postDto) {
        return ResponseEntity.ok(postService.updatePost(id, postDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok("Post deleted successfully!");
    }
}
