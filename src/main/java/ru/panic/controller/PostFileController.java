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

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostFileController {

    private final PostFileService postFileService;

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

    @PostMapping("/{postId}/file")
    public List<PostFileDto> create(@PathVariable("postId") Long postId, @RequestPart("files") List<MultipartFile> files) {
        return postFileService.create(postId, files);
    }

    @DeleteMapping("/file")
    public void deleteByObjectKey(@RequestParam("object_key") String objectKey) {
        postFileService.deleteByObjectKey(objectKey);
    }

}
