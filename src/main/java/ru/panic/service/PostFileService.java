package ru.panic.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.panic.dto.PostFileDto;
import ru.panic.entity.Post;
import ru.panic.entity.PostFile;
import ru.panic.mapper.PostFileToPostFileDtoMapperImpl;
import ru.panic.repository.PostFileRepository;
import ru.panic.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostFileService {

    private final PostRepository postRepository;
    private final PostFileRepository postFileRepository;
    private final PostFileToPostFileDtoMapperImpl postFileToPostFileDtoMapper;
    private final S3Service s3Service;

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(".png", ".jpg", ".jpeg");

    public byte[] getByObjectKey(String objectKey) {
        return s3Service.downloadFile("posts", objectKey);
    }

    @Transactional
    public List<PostFileDto> create(Long postId, List<MultipartFile> files) {
        if (!areExtensionsValid(files)) {
            throw new IllegalArgumentException("Unsupported extension type");
        }
        List<PostFile> newPostFiles = new ArrayList<>();
        Post post = postRepository.findById(postId).orElseThrow();
        for (MultipartFile file : files) {
            String objectKey = s3Service.uploadFile("posts", file);
            newPostFiles.add(
                    PostFile.builder()
                            .objectKey(objectKey)
                            .post(post)
                            .build()
            );
        }
        return postFileToPostFileDtoMapper.postFileListToPostFileDtoList(
                postFileRepository.saveAll(newPostFiles)
        );
    }

    @Transactional
    public void deleteByObjectKey(String objectKey) {
        s3Service.deleteFile("posts", objectKey);
        postFileRepository.deleteByObjectKey(objectKey);
    }

    private boolean areExtensionsValid(List<MultipartFile> files) {
        for (MultipartFile file : files) {
            String filename = file.getOriginalFilename();
            if (!filename.contains(".")) {
                return false;
            }
            String extension = filename.substring(filename.lastIndexOf(".")).toLowerCase();
            if (!ALLOWED_EXTENSIONS.contains(extension)) {
                return false;
            }
        }
        return true;
    }

}
