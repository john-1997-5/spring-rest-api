package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Autowired // se pude omitir
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;

    }

    @Override
    public CommentDto createComment(Long postId, CommentDto commentDto) {
        Comment comment = mapToEntity(commentDto);
        Post relatedPost = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", String.valueOf(postId)));
        comment.setPost(relatedPost);
        return mapToDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentDto> getPostComments(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(this::mapToDto)
                .toList();
    }

    private Comment mapToEntity(CommentDto commentDto) {
        Comment newComment = new Comment();
        newComment.setName(commentDto.getName());
        newComment.setEmail(commentDto.getEmail());
        newComment.setBody(commentDto.getBody());
        return newComment;
    }

    private CommentDto mapToDto(Comment comment) {
        CommentDto newCommentDto = new CommentDto();
        newCommentDto.setId(comment.getId());
        newCommentDto.setName(comment.getName());
        newCommentDto.setEmail(comment.getEmail());
        newCommentDto.setBody(comment.getBody());
        return newCommentDto;
    }
}
