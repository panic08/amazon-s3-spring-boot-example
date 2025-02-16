package ru.panic.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.panic.dto.PostDto;
import ru.panic.payload.request.CreatePostRequest;
import ru.panic.payload.request.UpdatePostRequest;
import ru.panic.service.PostService;

/**
 * REST controller for managing posts.
 */
@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * Retrieves a post by its ID.
     *
     * @param id the ID of the post
     * @return the post details
     */
    @GetMapping("/{id}")
    public PostDto get(@PathVariable("id") Long id) {
        return postService.get(id);
    }

    /**
     * Creates a new post.
     *
     * @param request the post creation request
     * @return the created post details
     */
    @PostMapping
    public PostDto create(@RequestBody CreatePostRequest request) {
        return postService.create(request);
    }

    /**
     * Updates an existing post by its ID.
     *
     * @param id the ID of the post
     * @param request the post update request
     * @return the updated post details
     */
    @PutMapping("/{id}")
    public PostDto update(@PathVariable("id") Long id, @RequestBody UpdatePostRequest request) {
        return postService.update(id, request);
    }

    /**
     * Deletes a post by its ID.
     *
     * @param id the ID of the post
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        postService.delete(id);
    }

}
