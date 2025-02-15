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

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostToPostDtoMapperImpl postToPostDtoMapper;
    private final S3Service s3Service;

    @Transactional
    public PostDto get(Long id) {
        return postToPostDtoMapper.postToPostDto(
                postRepository.findById(id)
                        .orElseThrow()
        );
    }

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

    @Transactional
    public PostDto update(Long id, UpdatePostRequest request) {
        Post post = postRepository.findById(id).orElseThrow();
        post.setTitle(request.title());
        post.setDescription(request.description());
        postRepository.save(post);
        return postToPostDtoMapper.postToPostDto(post);
    }

    @Transactional
    public void delete(Long id) {
        List<PostFile> files = postRepository.findById(id).orElseThrow().getFiles();
        for (PostFile file : files) {
            s3Service.deleteFile("posts", file.getObjectKey());
        }
        postRepository.deleteById(id);
    }

}
