package ru.panic.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.panic.dto.PostFileDto;
import ru.panic.service.PostFileService;

import java.net.URLConnection;
import java.util.List;

/**
 * REST controller for managing post files.
 */
@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostFileController {

    private final PostFileService postFileService;

    /**
     * Retrieves a file by its object key.
     *
     * @param objectKey the object key of the file
     * @return the file as a byte array with appropriate headers
     */
    @GetMapping("/file")
    public ResponseEntity<byte[]> getByObjectKey(@RequestParam("object_key") String objectKey) {
        String contentType = URLConnection.guessContentTypeFromName(objectKey);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentDisposition(ContentDisposition.attachment().filename(objectKey).build());
        return ResponseEntity.ok()
                .headers(headers)
                .body(postFileService.getByObjectKey(objectKey));
    }

    /**
     * Uploads files for a specific post.
     *
     * @param postId the ID of the post
     * @param files the list of files to upload
     * @return the details of the uploaded files
     */
    @PostMapping("/{postId}/file")
    public List<PostFileDto> create(@PathVariable("postId") Long postId, @RequestPart("files") List<MultipartFile> files) {
        return postFileService.create(postId, files);
    }

    /**
     * Deletes a file by its object key.
     *
     * @param objectKey the object key of the file to delete
     */
    @DeleteMapping("/file")
    public void deleteByObjectKey(@RequestParam("object_key") String objectKey) {
        postFileService.deleteByObjectKey(objectKey);
    }

}
