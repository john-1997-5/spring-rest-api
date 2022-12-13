package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
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
        Post relatedPost = postDoesExist(postId);
        comment.setPost(relatedPost);
        return mapToDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentDto> getPostComments(Long postId) {
        //just for checking if a post with that id exists
        Post post = postDoesExist(postId);
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public CommentDto getPostCommentsById(Long postId, Long commentId) {
        Post post = postDoesExist(postId);
        Comment comment = commentDoesExist(commentId);
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "There is no such a comment for this post");
        }
        return mapToDto(comment);
    }

    @Override
    public CommentDto updateCommentById(Long postId, Long commentId, CommentDto commentDto) {
        Post post = postDoesExist(postId);
        Comment comment = commentDoesExist(commentId);
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "There is no such a comment for this post");
        }
        Optional.ofNullable(commentDto.getName()).ifPresent(comment::setName);
        Optional.ofNullable(commentDto.getEmail()).ifPresent(comment::setEmail);
        Optional.ofNullable(commentDto.getBody()).ifPresent(comment::setBody);
        Comment updatedComment = commentRepository.save(comment);
        return mapToDto(updatedComment);
    }

    @Override
    public void deleteCommentById(Long postId, Long commentId) {
        Post post = postDoesExist(postId);
        Comment comment = commentDoesExist(commentId);
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "There is no such a comment for this post");
        }

        commentRepository.delete(comment);
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

    private Post postDoesExist(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", String.valueOf(postId)));
    }

    private Comment commentDoesExist(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", String.valueOf(commentId)));
    }

}
