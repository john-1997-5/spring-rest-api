package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;

    @Override
    public PostDto createPost(PostDto postDtoRequest) {

        // convert dto to entity
        Post post = new Post();
        post.setTitle(postDtoRequest.getTitle());
        post.setDescription(postDtoRequest.getDescription());
        post.setContent(postDtoRequest.getContent());
        Post savedPost = postRepository.save(post);

        // convert entity to dto
        PostDto postDtoResponse = new PostDto();
        postDtoResponse.setId(savedPost.getId());
        postDtoResponse.setTitle(savedPost.getTitle());
        postDtoResponse.setDescription(savedPost.getDescription());
        postDtoResponse.setContent(savedPost.getContent());

        return postDtoResponse;
    }
}
