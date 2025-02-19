package ru.panic.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.panic.dto.PostFileDto;
import ru.panic.entity.Post;
import ru.panic.mapper.PostFileToPostFileDtoMapper;
import ru.panic.repository.PostFileRepository;
import ru.panic.repository.PostRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostFileServiceTest {
    @Mock
    private PostRepository postRepository;

    @Mock
    private PostFileRepository postFileRepository;

    @Mock
    private PostFileToPostFileDtoMapper postFileToPostFileDtoMapper;

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private PostFileService postFileService;

    private Post post;

    @BeforeEach
    void setUp() {
        post = Post.builder().id(1L).title("Test Post").description("Description").build();
    }

    @Test
    void testGetByObjectKey() {
        String objectKey = "test.png";
        byte[] fileData = new byte[]{1, 2, 3};

        when(s3Service.downloadFile("posts", objectKey)).thenReturn(fileData);

        byte[] result = postFileService.getByObjectKey(objectKey);

        assertArrayEquals(fileData, result);
        verify(s3Service, times(1)).downloadFile("posts", objectKey);
    }

    @Test
    void testCreateWithValidFiles() {
        MultipartFile file = new MockMultipartFile("file", "test.png", "image/png", new byte[]{1, 2, 3});
        List<MultipartFile> files = List.of(file);
        String objectKey = "uploaded-test.png";

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(s3Service.uploadFile("posts", file)).thenReturn(objectKey);
        when(postFileRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));
        when(postFileToPostFileDtoMapper.postFileListToPostFileDtoList(anyList())).thenReturn(List.of(new PostFileDto(1L, objectKey)));

        List<PostFileDto> result = postFileService.create(1L, files);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(objectKey, result.get(0).objectKey());

        verify(postRepository, times(1)).findById(1L);
        verify(s3Service, times(1)).uploadFile("posts", file);
        verify(postFileRepository, times(1)).saveAll(anyList());
        verify(postFileToPostFileDtoMapper, times(1)).postFileListToPostFileDtoList(anyList());
    }

    @Test
    void testCreateWithInvalidExtension() {
        MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", new byte[]{1, 2, 3});
        List<MultipartFile> files = List.of(file);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> postFileService.create(1L, files));
        assertEquals("Unsupported extension type", exception.getMessage());
    }

    @Test
    void testDeleteByObjectKey() {
        String objectKey = "test.png";

        doNothing().when(s3Service).deleteFile("posts", objectKey);
        doNothing().when(postFileRepository).deleteByObjectKey(objectKey);

        postFileService.deleteByObjectKey(objectKey);

        verify(s3Service, times(1)).deleteFile("posts", objectKey);
        verify(postFileRepository, times(1)).deleteByObjectKey(objectKey);
    }
}
