package ru.panic.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.panic.dto.PostDto;
import ru.panic.entity.Post;
import ru.panic.entity.PostFile;
import ru.panic.mapper.PostToPostDtoMapperImpl;
import ru.panic.payload.request.CreatePostRequest;
import ru.panic.payload.request.UpdatePostRequest;
import ru.panic.repository.PostRepository;

import java.util.List;

/**
 * Service class for managing posts.
 */
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostToPostDtoMapperImpl postToPostDtoMapper;
    private final S3Service s3Service;

    /**
     * Retrieves a post by its ID.
     *
     * @param id the ID of the post
     * @return the post details
     */
    @Transactional
    public PostDto get(Long id) {
        return postToPostDtoMapper.postToPostDto(
                postRepository.findById(id)
                        .orElseThrow()
        );
    }

    /**
     * Creates a new post.
     *
     * @param request the post creation request
     * @return the created post details
     */
    @Transactional
    public PostDto create(CreatePostRequest request) {
        Post newPost = Post.builder()
                .title(request.title())
                .description(request.description())
                .build();
        return postToPostDtoMapper.postToPostDto(
                postRepository.save(newPost)
        );
    }

    /**
     * Updates an existing post by its ID.
     *
     * @param id the ID of the post
     * @param request the post update request
     * @return the updated post details
     */
    @Transactional
    public PostDto update(Long id, UpdatePostRequest request) {
        Post post = postRepository.findById(id).orElseThrow();
        post.setTitle(request.title());
        post.setDescription(request.description());
        postRepository.save(post);
        return postToPostDtoMapper.postToPostDto(post);
    }

    /**
     * Deletes a post by its ID, including associated files in S3.
     *
     * @param id the ID of the post
     */
    @Transactional
    public void delete(Long id) {
        List<PostFile> files = postRepository.findById(id).orElseThrow().getFiles();
        for (PostFile file : files) {
            s3Service.deleteFile("posts", file.getObjectKey());
        }
        postRepository.deleteById(id);
    }

}
