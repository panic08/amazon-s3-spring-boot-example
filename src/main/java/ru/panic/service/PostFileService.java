package ru.panic.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.panic.dto.PostFileDto;
import ru.panic.entity.Post;
import ru.panic.entity.PostFile;
import ru.panic.mapper.PostFileToPostFileDtoMapper;
import ru.panic.repository.PostFileRepository;
import ru.panic.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Service class for handling file operations related to posts.
 */
@Service
@RequiredArgsConstructor
public class PostFileService {

    private final PostRepository postRepository;
    private final PostFileRepository postFileRepository;
    private final PostFileToPostFileDtoMapper postFileToPostFileDtoMapper;
    private final S3Service s3Service;

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(".png", ".jpg", ".jpeg");

    /**
     * Retrieves a file from S3 by its object key.
     *
     * @param objectKey the object key of the file
     * @return the file as a byte array
     */
    public byte[] getByObjectKey(String objectKey) {
        return s3Service.downloadFile("posts", objectKey);
    }

    /**
     * Uploads files for a post and stores metadata.
     *
     * @param postId the ID of the post
     * @param files the list of files to upload
     * @return the details of the uploaded files
     */
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

    /**
     * Deletes a file from S3 and removes its metadata.
     *
     * @param objectKey the object key of the file
     */
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
