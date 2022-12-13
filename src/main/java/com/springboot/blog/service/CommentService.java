package com.springboot.blog.service;

import com.springboot.blog.payload.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(Long postId, CommentDto commentDto);

    List<CommentDto> getPostComments(Long postId);

    CommentDto getPostCommentsById(Long postId, Long commentId);

}
