package com.springboot.blog.controller;

import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{postId}/comment")
    public ResponseEntity<CommentDto> createComment(@PathVariable Long postId, @Valid @RequestBody CommentDto commentDto) {
        CommentDto newComment = commentService.createComment(postId, commentDto);
        return new ResponseEntity<>(newComment, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getPostComments(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getPostComments(postId));
    }

    @GetMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> getPostCommentById(@PathVariable Long postId, @PathVariable Long commentId) {
        CommentDto commentDto = commentService.getPostCommentsById(postId, commentId);
        return ResponseEntity.ok(commentDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateCommentById(@PathVariable Long postId,
                                                        @PathVariable Long commentId,
                                                        @Valid @RequestBody CommentDto commentDto) {

        return ResponseEntity.ok(commentService.updateCommentById(postId, commentId, commentDto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteCommentById(@PathVariable Long postId,
                                                    @PathVariable Long commentId) {
        commentService.deleteCommentById(postId, commentId);
        return ResponseEntity.ok("comment with id : " + commentId + " of post with id : " + postId + " deleted successfully!");

    }
}

