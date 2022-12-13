package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy) // ASC order por default
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Post> postsPage = postRepository.findAll(pageable);

        List<PostDto> content = postsPage.getContent().stream()
                .map(this::mapToDto)
                .toList();

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(postsPage.getNumber());
        postResponse.setPageSize(postsPage.getSize());
        postResponse.setTotalElements(postsPage.getTotalElements());
        postResponse.setTotalPages(postsPage.getTotalPages());
        postResponse.setLast(postResponse.isLast());
        return postResponse;
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

    @Override
    public void deletePost(Long id) {
        Post postToDelete = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", String.valueOf(id)));
        postRepository.delete(postToDelete);
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
