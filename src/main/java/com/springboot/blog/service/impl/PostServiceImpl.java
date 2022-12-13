package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Override
    public PostDto createPost(PostDto postDtoRequest) {
        // convert dto to entity
        Post savedPost = postRepository.save(mapToEntity(postDtoRequest));

        // convert entity to dto
        return mapToDto(savedPost);
    }

    @Override
    public List<PostDto> getAllPosts() {
        List<Post> posts = postRepository.findAll();

        return posts.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PostDto getPostById(Long id) {
        return mapToDto(postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", String.valueOf(id))));
    }

    @Override
    public PostDto updatePost(Long id, PostDto postDto) {
        Post postToUpdate = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", String.valueOf(id)));

        postToUpdate.setTitle(postDto.getTitle());
        postToUpdate.setDescription(postDto.getDescription());
        postToUpdate.setContent(postDto.getContent());

        return mapToDto(postRepository.save(postToUpdate));
    }

    private Post mapToEntity(PostDto postDto) {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        return post;
    }

    private PostDto mapToDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setDescription(post.getDescription());
        postDto.setContent(post.getContent());
        return postDto;
    }
}
