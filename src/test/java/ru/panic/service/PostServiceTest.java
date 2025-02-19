package ru.panic.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.panic.dto.PostDto;
import ru.panic.entity.Post;
import ru.panic.entity.PostFile;
import ru.panic.mapper.PostToPostDtoMapper;
import ru.panic.payload.request.CreatePostRequest;
import ru.panic.payload.request.UpdatePostRequest;
import ru.panic.repository.PostRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostToPostDtoMapper postToPostDtoMapper;

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private PostService postService;

    private Post post;
    private PostDto postDto;

    @BeforeEach
    void setUp() {
        post = Post.builder().id(1L).title("Test Post").description("Description").build();
        postDto = new PostDto(1L, "Test Post", "Description", List.of());
    }

    @Test
    void testGet() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postToPostDtoMapper.postToPostDto(post)).thenReturn(postDto);

        PostDto result = postService.get(1L);

        assertNotNull(result);
        assertEquals(postDto, result);
        verify(postRepository, times(1)).findById(1L);
        verify(postToPostDtoMapper, times(1)).postToPostDto(post);
    }

    @Test
    void testCreate() {
        CreatePostRequest request = new CreatePostRequest("New Post", "New Description");
        Post newPost = Post.builder().title(request.title()).description(request.description()).build();
        PostDto newPostDto = new PostDto(2L, "New Post", "New Description", List.of());

        when(postRepository.save(any(Post.class))).thenReturn(newPost);
        when(postToPostDtoMapper.postToPostDto(newPost)).thenReturn(newPostDto);

        PostDto result = postService.create(request);

        assertNotNull(result);
        assertEquals(newPostDto, result);
        verify(postRepository, times(1)).save(any(Post.class));
        verify(postToPostDtoMapper, times(1)).postToPostDto(newPost);
    }

    @Test
    void testUpdate() {
        UpdatePostRequest request = new UpdatePostRequest("Updated Title", "Updated Description");

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postRepository.save(post)).thenReturn(post);
        when(postToPostDtoMapper.postToPostDto(post)).thenReturn(postDto);

        PostDto result = postService.update(1L, request);

        assertNotNull(result);
        assertEquals(postDto, result);
        assertEquals("Updated Title", post.getTitle());
        assertEquals("Updated Description", post.getDescription());
        verify(postRepository, times(1)).findById(1L);
        verify(postRepository, times(1)).save(post);
        verify(postToPostDtoMapper, times(1)).postToPostDto(post);
    }

    @Test
    void testDelete() {
        PostFile file = PostFile.builder().id(1L).objectKey("test.png").post(post).build();
        post.setFiles(List.of(file));

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        doNothing().when(s3Service).deleteFile("posts", file.getObjectKey());
        doNothing().when(postRepository).deleteById(1L);

        postService.delete(1L);

        verify(postRepository, times(1)).findById(1L);
        verify(s3Service, times(1)).deleteFile("posts", file.getObjectKey());
        verify(postRepository, times(1)).deleteById(1L);
    }
}
